package at.fhv.teame.middleware;


import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.CustomerServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.Properties;

@Stateless
public class SearchCustomerServiceImpl implements SearchCustomerServiceRemote {

    private final CustomerServiceRemote searchCustomerServiceRemoteStub;

    public SearchCustomerServiceImpl(){
        Object obj = null;
        try {
            final Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
            InitialContext ctx = new InitialContext(jndiProperties);
            obj = ctx.lookup("ejb:/music-shop-backend-customer-1.0-SNAPSHOT/SearchCustomerServiceImpl!at.fhv.teame.sharedlib.ejb.CustomerServiceRemote");
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            searchCustomerServiceRemoteStub = (CustomerServiceRemote) obj;
        }
    }

    public SearchCustomerServiceImpl(CustomerServiceRemote customerService){
        this.searchCustomerServiceRemoteStub = customerService;
    }

    @Override
    public List<CustomerDTO> getCustomerByFullName(String givenName, String familyName, int pageNr) {
        return searchCustomerServiceRemoteStub.getCustomerByFullName(givenName, familyName, pageNr);
    }

    @Override
    public List<CustomerDTO> getCustomerByFamilyName(String familyName, int pageNr) {
        return searchCustomerServiceRemoteStub.getCustomerByFamilyName(familyName, pageNr);
    }

    @Override
    public int totResultsByFullName(String givenName, String familyName) {
        return searchCustomerServiceRemoteStub.totResultsByFullName(givenName, familyName);
    }

    @Override
    public int totResultsByFamilyName(String familyName) {
        return searchCustomerServiceRemoteStub.totResultsByFamilyName(familyName);
    }
}
