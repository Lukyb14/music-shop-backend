package at.fhv.teame.application.impl.digitalsong;


import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseDigitalSongServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidCredentialsException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;


@Stateless
public class PurchaseDigitalSongServiceImpl implements PurchaseDigitalSongServiceRemote {

    @EJB
    private SearchCustomerServiceRemote searchCustomerService;

    @EJB
    private DigitalSongRepository digitalSongRepository;

    public PurchaseDigitalSongServiceImpl() { }

    @Override
    public void purchaseSong(Map<String, Integer> purchasedSongIds, String email, String cvc) throws InvalidCredentialsException {
//        if (purchasedSongs.isEmpty()) {
//            throw new PurchaseFailedException();
//        }

        //check if customer credentials are valid, else throw exception
        CustomerDTO customerDTO = null;
        try {
            customerDTO = searchCustomerService.getCustomerByEmailAndCvc(email, cvc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidCredentialsException();
        }

        HashMap<DigitalSong, Integer> digitalSongList = new HashMap<>();

        for(Map.Entry<String, Integer> entry : purchasedSongIds.entrySet()) {
            DigitalSong digitalSong = digitalSongRepository.digitalSongByArticleId(Long.valueOf(entry.getKey()));
            digitalSongList.put(digitalSong, entry.getValue());
        }

        DigitalInvoice digitalInvoice = new DigitalInvoice(email, digitalSongList);

        //Ensure Business invariants & Store Invoice


        System.out.println(customerDTO.getEmail());
    }
}
