package at.fhv.teame.application.impl;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.connection.Session;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.ejb.LoggedInClientRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

@Stateless
public class AuthenticationServiceImpl implements AuthenticationServiceRemote {

    private static final String PROVIDER_URL = "ldap://10.0.40.169:389";
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public static Map<ClientUser, LoggedInClientRemote> loggedInClientsMap;

    public AuthenticationServiceImpl() {
        this(new HibernateUserRepository(), new ListSessionRepository());
        loggedInClientsMap = new HashMap();
    }

    public AuthenticationServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public SessionDTO backdoorLogin(String username) {
        // TODO remove in production
        try {
            ClientUser clientUser = userRepository.userByCn(username);
            Session session = sessionRepository.createSession(clientUser);

            return new SessionDTO(session.getSessionId().toString(), clientUser.getRole().toString());
        } catch (UserNotFoundException e) {
            return null;
        }
    }

    @Override
    public SessionDTO login(String username, String password) throws LoginFailedException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, PROVIDER_URL);

        try {
            InitialDirContext ctx = new InitialDirContext(properties);
            String base = " ,dc=ad,dc=teame,dc=net";

            //Search in different ous for user
            String[] ous = {"ou=employees"};
            String distinguishedName = null;
            for (int i = 0; i < ous.length; i++) {
                distinguishedName = searchUserInBase(ctx, username, ous[i] + base);
                if (distinguishedName != null) break;
            }

            if(distinguishedName == null) throw new LoginFailedException();

            properties.put(Context.SECURITY_PRINCIPAL, distinguishedName);
            properties.put(Context.SECURITY_CREDENTIALS, password);
            try {
                ctx = new InitialDirContext(properties);
                System.out.println("Connection to LDAP System successful");

                ClientUser clientUser = userRepository.userByCn(username);
                Session session = sessionRepository.createSession(clientUser);

                return new SessionDTO(session.getSessionId().toString(), clientUser.getRole().toString());
            } catch (Exception e) {
                // invalid password
                throw new LoginFailedException();
            } finally {
                ctx.close();
            }
        } catch (NamingException e) {
            // invalid user or password
            throw new LoginFailedException();
        }
    }

    @Override
    public void logout(String sessionId) {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            loggedInClientsMap.remove(session.getUser());
            sessionRepository.deleteSession(UUID.fromString(sessionId));
        } catch (IllegalArgumentException | SessionNotFoundException ignored) {
            // if we can't create a UUID from the string, it is invalid and therefore does not exist anyway
        }
    }


    @Override
    public void rememberClient(LoggedInClientRemote loggedInClient, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            loggedInClientsMap.put(session.getUser(), loggedInClient);
        } catch (SessionNotFoundException e) {
            throw new InvalidSessionException();
        }
    }


    private String searchUserInBase(InitialDirContext ctx, String username, String base) throws NamingException {
        Attributes match = new BasicAttributes();
        //3. Search the directory using an appropriate filter to identify the record for the specific login name provided
        match.put(new BasicAttribute("cn", username));
        NamingEnumeration<SearchResult> searchResults = ctx.search(base, match);

        String distinguishedName;
        //4. If exactly one entry is returned, get the DN of the entry
        if (searchResults.hasMore()) {
            SearchResult result = searchResults.next();
            distinguishedName = result.getNameInNamespace();
            return distinguishedName;
        } else {  //if zero, or more than one entries are returned → output: "no such user"
            System.out.println("no such user");
            return null;
        }
    }
}
