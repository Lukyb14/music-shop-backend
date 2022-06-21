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

    private CustomerServiceRemote customerServiceRemote;

    public SearchCustomerServiceImpl(){
        Object obj = null;
        try {
            final Properties jndiProperties = new Properties();
            jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            jndiProperties.put(Context.PROVIDER_URL, "http-remoting://10.0.40.169:8080");
            InitialContext ctx = new InitialContext(jndiProperties);
            obj = ctx.lookup("ejb:/music-shop-backend-customer-1.0-SNAPSHOT/SearchCustomerServiceImpl!at.fhv.teame.sharedlib.ejb.CustomerServiceRemote");
        } catch (NamingException e) {
            e.printStackTrace();
        } finally {
            customerServiceRemote = (CustomerServiceRemote) obj;
        }
    }

    @Override
    public List<CustomerDTO> getCustomerByFullName(String givenName, String familyName, int pageNr) {
        return customerServiceRemote.getCustomerByFullName(givenName, familyName, pageNr);
    }

    @Override
    public List<CustomerDTO> getCustomerByFamilyName(String familyName, int pageNr) {
        return customerServiceRemote.getCustomerByFamilyName(familyName, pageNr);
    }

    @Override
    public CustomerDTO getCustomerByCreditCardNumberAndCvc(String creditCardNumber, String cvc) {
        return customerServiceRemote.getCustomerByCreditCardNumberAndCvc(creditCardNumber, cvc);
    }

    @Override
    public int totResultsByFullName(String givenName, String familyName) {
        return customerServiceRemote.totResultsByFullName(givenName, familyName);
    }

    @Override
    public int totResultsByFamilyName(String familyName) {
        return customerServiceRemote.totResultsByFamilyName(familyName);
    }
}
