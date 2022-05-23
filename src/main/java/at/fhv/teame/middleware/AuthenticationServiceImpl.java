package at.fhv.teame.middleware;

import at.fhv.teame.middleware.api.AuthenticationServiceLocal;
import at.fhv.teame.domain.model.session.Session;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.ws.rs.NotFoundException;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Stateless
public class AuthenticationServiceImpl implements AuthenticationServiceRemote, AuthenticationServiceLocal {
    @EJB
    private UserRepository userRepository;
    @EJB
    private SessionRepository sessionRepository;
    private static final String PROVIDER_URL = "ldap://10.0.40.169:389";

    public AuthenticationServiceImpl() { };

    // For testing
    public AuthenticationServiceImpl(UserRepository userRepository, SessionRepository sessionRepository){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public SessionDTO login(String username, String password) throws LoginFailedException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, PROVIDER_URL);

        try {
            InitialDirContext ctx = new InitialDirContext(properties);
            String base = "ou=employees,dc=ad,dc=teame,dc=net";

            String dn = searchUserInBase(ctx, username, base, "uid");

            if(dn == null) throw new NamingException();

            properties.put(Context.SECURITY_PRINCIPAL, dn);
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
    public String loginCustomer(String givenMail, String password) throws LoginFailedException {
        Properties properties = new Properties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        properties.put(Context.PROVIDER_URL, PROVIDER_URL);

        try {
            InitialDirContext ctx = new InitialDirContext(properties);
            String base = " ,dc=ad,dc=teame,dc=net";

            //Search in different ous for user
            String[] ous = {"ou=customer", "ou=licensee"};
            String dn = null;
            String ou = null;
            for (int i = 0; i < ous.length; i++) {
                ou = ous[i];
                dn = searchUserInBase(ctx, givenMail, ou + base, "mail");
                if (dn != null) break;
            }

            if(dn == null) throw new NamingException();

            properties.put(Context.SECURITY_PRINCIPAL, dn);
            properties.put(Context.SECURITY_CREDENTIALS, password);
            try {
                ctx = new InitialDirContext(properties);
                System.out.println("Connection to LDAP System successful");
                System.out.println("dn: "+ dn);

                Pattern mailPattern = Pattern.compile("\\w+@email\\.test"); //regex to extract mail from dn
                Matcher matcher = mailPattern.matcher(dn);

                if (matcher.find()) {
                    return matcher.group();
                }
                throw new NotFoundException();
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

    private String searchUserInBase(InitialDirContext ctx, String username, String base, String attribute) throws NamingException {
        Attributes match = new BasicAttributes();
        match.put(new BasicAttribute(attribute, username));
        NamingEnumeration<SearchResult> searchResults = ctx.search(base, match);

        String dn;
        if (searchResults.hasMore()) {
            SearchResult result = searchResults.next();
            dn = result.getNameInNamespace();
            return dn;
        } else {
            System.out.println("no such user");
            return null;
        }
    }
}
