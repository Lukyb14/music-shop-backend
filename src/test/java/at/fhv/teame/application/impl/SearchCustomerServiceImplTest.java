package at.fhv.teame.application.impl;

import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.rmi.SearchCustomerService;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import org.junit.jupiter.api.Test;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

class SearchCustomerServiceImplTest {

    private SearchCustomerService searchCustomerService;

/*    @Test
    void getCustomerByFullName() throws RemoteException {
        searchCustomerService = new SearchCustomerServiceImpl();

//        List<CustomerDTO> customerDTOS =  searchCustomerService.getCustomerByFullName("jos", "sto");
//
//        for(CustomerDTO customerDTO : customerDTOS) {
//            System.out.println(customerDTO.getGivenName());
//            System.out.println(customerDTO.getFamilyName());
//            System.out.println(customerDTO.getBirthdate());
//            System.out.println(customerDTO.getAddress());
//        }

    }

    @Test
    void getCustomerByFamilyName() {
    }*/
}