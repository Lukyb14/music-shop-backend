package at.fhv.teame.integration;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.middleware.AuthenticationServiceImpl;
import at.fhv.teame.middleware.MessagingServiceImpl;
import at.fhv.teame.middleware.SearchCustomerServiceImpl;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.ejb.MessageServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchCustomerServiceRemote;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import at.fhv.teame.sharedlib.exceptions.PublishingFailedException;
import at.fhv.teame.sharedlib.exceptions.ReceiveFailedException;
import at.fhv.teame.sharedlib.rmi.*;
import at.fhv.teame.sharedlib.exceptions.*;
import at.fhv.teame.sharedlib.rmi.factory.RMIFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {
    private static AuthenticationServiceRemote authenticationService;
    private static MessageServiceRemote messagingService;
    private static PurchaseSoundCarrierService purchaseSoundCarrierService;
    private static SearchCustomerServiceRemote searchCustomerService;
    private static SearchInvoiceService searchInvoiceService;
    private static SearchSoundCarrierServiceRemote searchSoundCarrierService;
    private static WithdrawSoundCarrierService withdrawSoundCarrierService;

    private static SoundCarrierRepository soundCarrierRepository;
    private static InvoiceRepository invoiceRepository;
    private static UserRepository userRepository;

    @BeforeAll
    static void setup() throws IOException {
       // RMIFactory rmiFactory = new RMIFactoryImpl();
//        authenticationService = rmiFactory.createSearchAuthenticationServiceImpl();
//        messagingService = rmiFactory.createMessageServiceImpl();
//        purchaseSoundCarrierService = rmiFactory.createPurchaseSoundCarrierServiceImpl();
//        searchCustomerService = rmiFactory.createSearchCustomerServiceImpl();
//        searchInvoiceService = rmiFactory.createSearchInvoiceServiceImpl();
//        searchSoundCarrierService = rmiFactory.createSearchSoundCarrierServiceImpl();
//        withdrawSoundCarrierService = rmiFactory.createWithdrawSoundCarrierServiceImpl();

        soundCarrierRepository = new HibernateSoundCarrierRepository();
        invoiceRepository = new HibernateInvoiceRepository();
        userRepository = new HibernateUserRepository();
        authenticationService = new AuthenticationServiceImpl(new HibernateUserRepository(), new ListSessionRepository());
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl(soundCarrierRepository);
        searchCustomerService = new SearchCustomerServiceImpl();
        messagingService = new MessagingServiceImpl();
    }

    @Test
    void soundCarriersByArtist(){


        List<SoundCarrierDTO> soundCarrierDTOs = searchSoundCarrierService.soundCarriersByArtistName("Cofer Brothers", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist(){

        int nrOfSoundCarriers = searchSoundCarrierService.totResultsByArtistName("AC/DC");
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum(){

        int nrOfSoundCarriers = searchSoundCarrierService.totResultsBySongName("Back In Black");
        System.out.println("count " + nrOfSoundCarriers);
    }



    @Test
    void getSoundCarriersByAlbum(){


        List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOS) {
            System.out.println(soundCarrierDTO.getAlbumName());
            System.out.println(soundCarrierDTO.getMedium());
        }
    }

    @Test
    void fetchMessages() throws LoginFailedException, InvalidSessionException, ReceiveFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<MessageDTO> messageDTOs = messagingService.fetchMessages(sessionDTO.getSessionId());

        assertTrue(messageDTOs.size() > 0);

        for (MessageDTO dto : messageDTOs) {
            assertEquals("System.Message", dto.getTopic());
        }
    }

    @Test
    void getCustomerByFamilyName(){


        List<CustomerDTO> customers = searchCustomerService.getCustomerByFamilyName("Hanf", 0);

        for (CustomerDTO customer : customers) {
            assertEquals("Hanf", customer.getFamilyName());
        }
    }

    @Test
    void getCustomerByFullName(){


        List<CustomerDTO> customers = searchCustomerService.getCustomerByFullName("Amira","Hanf", 0);

        assertEquals("Hanf", customers.get(0).getFamilyName());
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


    @Test
    void publishMessage() throws LoginFailedException, InvalidSessionException, ReceiveFailedException, PublishingFailedException, InterruptedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        String randomValue = UUID.randomUUID().toString();
        messagingService.publishMessage(new MessageDTO("Rock", "New Concert", randomValue));

        // wait 1 second for ActiveMQ to update itself
        Thread.sleep(6000);

        List<MessageDTO> messagesAfter = messagingService.fetchMessages(sessionDTO.getSessionId());
        boolean isMessagePresent = false;
        for (MessageDTO message : messagesAfter) {
            if (message.getContent().equals(randomValue)) {
                isMessagePresent = true;
                break;
            }
        }

        assertTrue(isMessagePresent);
    }

    @Test
    void deleteMessage() throws LoginFailedException, InvalidSessionException, ReceiveFailedException, DeletionFailedException, InterruptedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        List<MessageDTO> messagesBeforeDeletion = messagingService.fetchMessages(sessionDTO.getSessionId());
        String deletedMessageId = messagesBeforeDeletion.get(0).getId();
        String deletedMessageTopic = messagesBeforeDeletion.get(0).getTopic();
      
        messagingService.deleteMessage(deletedMessageTopic, deletedMessageId, sessionDTO.getSessionId());

        // wait 4 seconds for ActiveMQ to update itself
        Thread.sleep(6000);

        List<MessageDTO> messagesAfterDeletion = messagingService.fetchMessages(sessionDTO.getSessionId());
        boolean isMessagePresent = false;
        for (MessageDTO message : messagesAfterDeletion) {
            if (message.getId().equals(deletedMessageId)) {
                isMessagePresent = true;
                break;
            }
        }

        assertFalse(isMessagePresent);
    }


    @Test
    void allTopics(){
        List<String> expectedTopics = List.of("Pop", "Rock", "System.Message");

        List<String> actualTopics = messagingService.allTopics();

        assertEquals(expectedTopics, actualTopics);
    }

    // infrastructure

    @Test
    void totResultsByAlbumName() {
        Long results = soundCarrierRepository.totResultsByAlbumName("Back in Black");

        assertTrue(results > 0);
    }

    @Test
    void soundCarriersBySongName() {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName("Asphalt", 1);

        soundCarriers.forEach(sc -> assertTrue(sc.getAlbum().getSongs().stream().anyMatch(song -> song.getTitle().equals("Asphalt"))));
    }

    @Test
    void soundCarrierByArticleId() {
        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("295748");

        assertEquals("295748", soundCarrier.getArticleId());
    }

    @Test
    void fillStock() {
        SoundCarrier soundCarrierBeforeFill = soundCarrierRepository.soundCarrierByArticleId("295748");
        int expectedStock = soundCarrierBeforeFill.getStock() + 1;

        soundCarrierRepository.fillStock("295748", 1);

        SoundCarrier soundCarrierAfterFill = soundCarrierRepository.soundCarrierByArticleId("295748");
        int actualStock = soundCarrierAfterFill.getStock();

        assertEquals(expectedStock, actualStock);
    }

    @Test
    void invoiceById() {
        Invoice invoice = invoiceRepository.invoiceById(20028L);

        assertEquals(20028L, invoice.getInvoiceId());
    }
}
