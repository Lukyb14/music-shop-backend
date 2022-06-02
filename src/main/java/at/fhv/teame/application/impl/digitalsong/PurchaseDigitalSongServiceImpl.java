package at.fhv.teame.application.impl.digitalsong;


import at.fhv.teame.application.impl.event.PurchasedDigitalSongEvent;
import at.fhv.teame.application.impl.event.dto.DigitalSongDTO;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalInvoiceRepository;
import at.fhv.teame.domain.repositories.DigitalSongRepository;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseDigitalSongServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidCredentialsException;
import at.fhv.teame.sharedlib.exceptions.PublishingFailedException;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;


@Stateless
public class PurchaseDigitalSongServiceImpl implements PurchaseDigitalSongServiceRemote {

    @EJB
    private SearchCustomerServiceRemote searchCustomerService;

    @EJB
    private DigitalSongRepository digitalSongRepository;

    @EJB
    private DigitalInvoiceRepository digitalInvoiceRepository;

    public PurchaseDigitalSongServiceImpl() { }

    @Override
    public void purchaseDigitalSong(String userId, List<String> purchasedSongIds, String creditCardNumber, String cvc) throws InvalidCredentialsException, PurchaseFailedException {
        if (purchasedSongIds.isEmpty()) throw new PurchaseFailedException();

        CustomerDTO customerDTO = null;
        try {
            customerDTO = searchCustomerService.getCustomerByCreditCardNumberAndCvc(creditCardNumber, cvc);
        } catch (Exception e) {
            throw new InvalidCredentialsException();
        }

        if (isDigitalSongAlreadyPurchased(purchasedSongIds, customerDTO.getEmail())) throw new PurchaseFailedException();

        List<DigitalInvoiceLine> digitalInvoiceLines = new ArrayList<>();
        List<DigitalSongDTO> digitalSongDTOList = new ArrayList<>();
        for (String songId : purchasedSongIds) {
            DigitalSong d = digitalSongRepository.digitalSongByArticleId(Long.valueOf(songId));

            digitalSongDTOList.add(new DigitalSongDTO(d.getId(), d.getArtist(), d.getTitle(), d.getGenre(), d.getDuration(), d.getReleaseDate(), d.getMp3File(), d.getCoverFile(), d.getPrice()));

            digitalInvoiceLines.add(new DigitalInvoiceLine(d, d.getPrice()));
        }

        DigitalInvoice digitalInvoice = new DigitalInvoice(customerDTO.getGivenName(), customerDTO.getFamilyName(), customerDTO.getEmail(), digitalInvoiceLines);
        digitalInvoiceRepository.store(digitalInvoice);

        PurchasedDigitalSongEvent purchasedDigitalSongEvent = new PurchasedDigitalSongEvent(userId, digitalSongDTOList);

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            String purchasedDigitalSongEventJson = ow.writeValueAsString(purchasedDigitalSongEvent);
            publishPurchasedDigitalSongEvent(purchasedDigitalSongEventJson);
        } catch (JsonProcessingException | PublishingFailedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDigitalSongAlreadyPurchased(List<String> purchasedSongIds, String email) {
        List<DigitalInvoice> digitalInvoices = digitalInvoiceRepository.digitalInvoicesByEmail(email);
        for (DigitalInvoice d : digitalInvoices) {
            for (DigitalInvoiceLine dil : d.getPurchasedItems()) {

                for (String s : purchasedSongIds) {
                    if (dil.getProduct().getId().toString().equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void publishPurchasedDigitalSongEvent(String purchasedDigitalSongEventJson) throws PublishingFailedException {
        try {
            // Get the JNDI Initial Context to do JNDI lookups
            InitialContext ctx = new InitialContext();
            // Get the ConnectionFactory by JNDI name
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            // get the Destination used to send the message by JNDI name
            Destination dest = (Destination) ctx.lookup("purchasedDigitalSongQueue");
            // Create a connection
            Connection con = cf.createConnection();
            // create a JMS session
            Session sess = con.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            // Create some Message and a MessageProducer with the session
            Message msg = sess.createTextMessage(purchasedDigitalSongEventJson);
            MessageProducer prod = sess.createProducer(dest);
            // send the message to the destination
            prod.send(msg);
            // Close the connection
            con.close();
            sess.close();
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            throw new PublishingFailedException();
        }
    }
}
