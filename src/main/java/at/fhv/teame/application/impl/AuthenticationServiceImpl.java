package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.rmi.Session;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.rmi.AuthenticationService;
import at.fhv.teame.sharedlib.rmi.exceptions.LoginFailedException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.UUID;

public class AuthenticationServiceImpl extends UnicastRemoteObject implements AuthenticationService {

    private static final String PROVIDER_URL = "ldap://10.0.40.169:389";
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public AuthenticationServiceImpl() throws RemoteException {
        userRepository = new HibernateUserRepository();
        sessionRepository = new ListSessionRepository();
    }

    @Override
    public SessionDTO login(String username, String password) throws RemoteException, LoginFailedException {

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
                e.printStackTrace();
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
    public void logout(UUID uuid) throws RemoteException {
        sessionRepository.deleteSession(uuid);
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
