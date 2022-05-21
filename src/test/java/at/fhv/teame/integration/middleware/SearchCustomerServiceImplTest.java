package at.fhv.teame.integration.middleware;

import at.fhv.teame.middleware.SearchCustomerServiceImpl;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import org.junit.jupiter.api.BeforeAll;

class SearchCustomerServiceImplTest {

    private static SearchCustomerServiceRemote searchCustomerService;

    @BeforeAll
    static void setup() {
        searchCustomerService = new SearchCustomerServiceImpl();
    }


    /*
    @Test
    void getCustomerByFullName(){
        List<CustomerDTO> customers = searchCustomerService.getCustomerByFullName("Amira","Hanf", 0);

        assertEquals("Hanf", customers.get(0).getFamilyName());
    }

    @Test
    void getCustomerByFamilyName(){
        List<CustomerDTO> customers = searchCustomerService.getCustomerByFamilyName("Hanf", 0);

        for (CustomerDTO customer : customers) {
            assertEquals("Hanf", customer.getFamilyName());
        }
    }

    @Test
    void getTotalResultByFamilyName(){
        int results = searchCustomerService.totResultsByFamilyName("Hanf");
        assertEquals(14, results);
    }

    @Test
    void getTotalResultsByFullName(){
        int resultSize = searchCustomerService.totResultsByFullName("Amira","Hanf");
        assertEquals(1, resultSize);
    }
    */

}