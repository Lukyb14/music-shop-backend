package at.fhv.teame.application.impl.digitalsong;


import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseDigitalSongServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.Map;


@Stateless
public class PurchaseDigitalSongServiceImpl implements PurchaseDigitalSongServiceRemote {

    @EJB
    private SearchCustomerServiceRemote searchCustomerService;

    public PurchaseDigitalSongServiceImpl() {
    }

    @Override
    public void purchaseSong(Map<String, Integer> purchasedSongs, String email, String cvc) throws PurchaseFailedException {
//        if (purchasedSongs.isEmpty()) {
//            throw new PurchaseFailedException();
//        }

        CustomerDTO customerDTO = searchCustomerService.getCustomerByEmailAndCvc(email, cvc);

        System.out.println(customerDTO.getEmail());
    }
}
