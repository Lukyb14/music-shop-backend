package at.fhv.teame.application.impl.digitalsong;


import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseDigitalSongServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidCredentialsException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Stateless
public class PurchaseDigitalSongServiceImpl implements PurchaseDigitalSongServiceRemote {

    @EJB
    private SearchCustomerServiceRemote searchCustomerService;

    @EJB
    private DigitalSongRepository digitalSongRepository;

    public PurchaseDigitalSongServiceImpl() { }

    @Override
    public void purchaseSong(List<String> purchasedSongIds, String email, String cvc) throws InvalidCredentialsException {
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

        List<DigitalInvoiceLine> digitalInvoiceLines = new ArrayList<>();

        DigitalInvoice digitalInvoice = new DigitalInvoice(email);

        for(String entry : purchasedSongIds) {
            DigitalSong digitalSong = digitalSongRepository.digitalSongByArticleId(Long.valueOf(entry));
            digitalInvoiceLines.add(new DigitalInvoiceLine(digitalInvoice, digitalSong, digitalSong.getPrice()));
        }

        digitalInvoice.setPurchasedItems(digitalInvoiceLines);

        //TODO: REFACTORING!!! make same as soundcarrier invoice
        //Ensure Business invariants & Store Invoice

        digitalInvoiceRepository.add(digitalInvoice);
        System.out.println(customerDTO.getEmail());
    }
}
