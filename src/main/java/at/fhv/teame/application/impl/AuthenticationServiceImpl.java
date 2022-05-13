package at.fhv.teame.application.impl;

import at.fhv.teame.application.api.AuthenticationServiceLocal;
import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.domain.model.session.Session;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Properties;
import java.util.UUID;

@Stateless
public class AuthenticationServiceImpl implements AuthenticationServiceRemote, AuthenticationServiceLocal {
    @EJB
    private UserRepository userRepository;
    @EJB
    private SessionRepository sessionRepository;
    private static final String PROVIDER_URL = "ldap://10.0.40.169:389";

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
            String base = "ou=employees,dc=ad,dc=teame,dc=net";

            String uid = searchUserInBase(ctx, username, base);

            if(uid == null) throw new LoginFailedException();

            properties.put(Context.SECURITY_PRINCIPAL, uid);
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
    public String loginCustomer(String username, String password) throws LoginFailedException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, PROVIDER_URL);

        try {
            InitialDirContext ctx = new InitialDirContext(properties);
            String base = " ,dc=ad,dc=teame,dc=net";

            //Search in different ous for user
            String[] ous = {"ou=customer", "ou=licensee"};
            String uid = null;
            for (int i = 0; i < ous.length; i++) {
                uid = searchUserInBase(ctx, username, ous[i] + base);
                if (uid != null) break;
            }

            if(uid == null) throw new LoginFailedException();

            properties.put(Context.SECURITY_PRINCIPAL, uid);
            properties.put(Context.SECURITY_CREDENTIALS, password);
            try {
                ctx = new InitialDirContext(properties);
                System.out.println("Connection to LDAP System successful");
                return uid;
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
            sessionRepository.deleteSession(UUID.fromString(sessionId));
        } catch (IllegalArgumentException ignored) {
            // if we can't create a UUID from the string, it is invalid and therefore does not exist anyway
        }
    }

    private String searchUserInBase(InitialDirContext ctx, String username, String base) throws NamingException {
        Attributes match = new BasicAttributes();
        //3. Search the directory using an appropriate filter to identify the record for the specific login name provided
        match.put(new BasicAttribute("uid", username));
        NamingEnumeration<SearchResult> searchResults = ctx.search(base, match);

        String uid;
        //4. If exactly one entry is returned, get the DN of the entry
        if (searchResults.hasMore()) {
            SearchResult result = searchResults.next();
            uid = result.getNameInNamespace();
            return uid;
        } else {  //if zero, or more than one entries are returned → output: "no such user"
            System.out.println("no such user");
            return null;
        }
    }
}
