package at.fhv.teame.application.impl.ejb;


import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.connection.Session;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.CustomerServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Stateless
public class SearchCustomerServiceEJB implements SearchCustomerServiceRemote {

    private final CustomerServiceRemote searchCustomerServiceRemoteStub;
    private final SessionRepository sessionRepository;

    public SearchCustomerServiceEJB(){
        Object obj = null;
        try {
            final Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            InitialContext ctx = new InitialContext(jndiProperties);
            obj = ctx.lookup("ejb:/music-shop-backend-customer-1.0-SNAPSHOT/CustomerServiceEJB!at.fhv.teame.sharedlib.ejb.CustomerServiceRemote");
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            searchCustomerServiceRemoteStub = (CustomerServiceRemote) obj;
            sessionRepository = new ListSessionRepository();
        }
    }

    public SearchCustomerServiceEJB(CustomerServiceRemote customerService, SessionRepository sessionRepository){
        this.searchCustomerServiceRemoteStub = customerService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<CustomerDTO> getCustomerByFullName(String givenName, String familyName, int pageNr, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerServiceRemoteStub.getCustomerByFullName(givenName, familyName, pageNr);
    }

    @Override
    public List<CustomerDTO> getCustomerByFamilyName(String familyName, int pageNr, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerServiceRemoteStub.getCustomerByFamilyName(familyName, pageNr);
    }

    @Override
    public int totResultsByFullName(String givenName, String familyName, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerServiceRemoteStub.totResultsByFullName(givenName, familyName);
    }

    @Override
    public int totResultsByFamilyName(String familyName, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerServiceRemoteStub.totResultsByFamilyName(familyName);
    }
}
