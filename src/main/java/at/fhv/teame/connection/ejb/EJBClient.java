package at.fhv.teame.connection.ejb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class EJBClient {

    private static InitialContext context;

    public static void initialEJB() {
        try {
            final Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            context = new InitialContext(jndiProperties);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static InitialContext getContext() {
        return context;
    }
}
