package at.fhv.teame.integration;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
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
    private static AuthenticationService authenticationService;
    private static MessageService messagingService;
    private static PurchaseSoundCarrierService purchaseSoundCarrierService;
    private static SearchCustomerService searchCustomerService;
    private static SearchInvoiceService searchInvoiceService;
    private static SearchSoundCarrierService searchSoundCarrierService;
    private static WithdrawSoundCarrierService withdrawSoundCarrierService;

    private static SoundCarrierRepository soundCarrierRepository;
    private static InvoiceRepository invoiceRepository;
    private static UserRepository userRepository;

    @BeforeAll
    static void setup() throws IOException {
        RMIFactory rmiFactory = new RMIFactoryImpl();
        authenticationService = rmiFactory.createSearchAuthenticationServiceImpl();
        messagingService = rmiFactory.createMessageServiceImpl();
        purchaseSoundCarrierService = rmiFactory.createPurchaseSoundCarrierServiceImpl();
        searchCustomerService = rmiFactory.createSearchCustomerServiceImpl();
        searchInvoiceService = rmiFactory.createSearchInvoiceServiceImpl();
        searchSoundCarrierService = rmiFactory.createSearchSoundCarrierServiceImpl();
        withdrawSoundCarrierService = rmiFactory.createWithdrawSoundCarrierServiceImpl();

        soundCarrierRepository = new HibernateSoundCarrierRepository();
        invoiceRepository = new HibernateInvoiceRepository();
        userRepository = new HibernateUserRepository();
    }

    @Test
    void soundCarriersByArtist() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<SoundCarrierDTO> soundCarrierDTOs = searchSoundCarrierService.soundCarriersByArtistName("Cofer Brothers", 1, sessionDTO.getSessionId());

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        int nrOfSoundCarriers = searchSoundCarrierService.totResultsByArtistName("AC/DC", sessionDTO.getSessionId());
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        int nrOfSoundCarriers = searchSoundCarrierService.totResultsBySongName("Back In Black", sessionDTO.getSessionId());
        System.out.println("count " + nrOfSoundCarriers);
    }



    @Test
    void getSoundCarriersByAlbum() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1, sessionDTO.getSessionId());

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOS) {
            System.out.println(soundCarrierDTO.getAlbumName());
            System.out.println(soundCarrierDTO.getMedium());
        }
    }

    @Test
    void fetchMessages() throws LoginFailedException, RemoteException, InvalidSessionException, ReceiveFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<MessageDTO> messageDTOs = messagingService.fetchMessages(sessionDTO.getSessionId());

        assertTrue(messageDTOs.size() > 0);

        for (MessageDTO dto : messageDTOs) {
            assertEquals("System.Message", dto.getTopic());
        }
    }

    @Test
    void getCustomerByFamilyName() throws LoginFailedException, RemoteException, InvalidSessionException {
        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<CustomerDTO> customers = searchCustomerService.getCustomerByFamilyName("Hanf", 0, sessionDTO.getSessionId());

        for (CustomerDTO customer : customers) {
            assertEquals("Hanf", customer.getFamilyName());
        }
    }

    @Test
    void getCustomerByFullName() throws LoginFailedException, RemoteException, InvalidSessionException {
        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<CustomerDTO> customers = searchCustomerService.getCustomerByFullName("Amira","Hanf", 0, sessionDTO.getSessionId());

        assertEquals("Hanf", customers.get(0).getFamilyName());
    }

    @Test
    void getTotalResultByFamilyName() throws RemoteException, LoginFailedException, InvalidSessionException {
        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        int results = searchCustomerService.totResultsByFamilyName("Hanf", sessionDTO.getSessionId());
        assertEquals(14, results);
    }

    @Test
    void getTotalResultsByFullName() throws LoginFailedException, RemoteException, InvalidSessionException {
        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        int resultSize = searchCustomerService.totResultsByFullName("Amira","Hanf", sessionDTO.getSessionId());

        assertEquals(1, resultSize);
    }

    @Test
    void throwInvalidSessionException() {
        assertThrows(InvalidSessionException.class, () -> searchCustomerService.getCustomerByFamilyName("Hanf", 0, UUID.randomUUID().toString()));
        assertThrows(InvalidSessionException.class, () -> searchCustomerService.getCustomerByFullName("Amira","Hanf", 0, UUID.randomUUID().toString()));
        assertThrows(InvalidSessionException.class, () -> searchCustomerService.totResultsByFamilyName("Hanf", UUID.randomUUID().toString()));
        assertThrows(InvalidSessionException.class, () -> searchCustomerService.totResultsByFullName("Amira", "Hanf", UUID.randomUUID().toString()));
    }

    @Test
    void publishMessage() throws LoginFailedException, RemoteException, InvalidSessionException, ReceiveFailedException, PublishingFailedException, InterruptedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        String randomValue = UUID.randomUUID().toString();
        messagingService.publishMessage(new MessageDTO("Rock", "New Concert", randomValue), sessionDTO.getSessionId());

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
    void deleteMessage() throws LoginFailedException, RemoteException, InvalidSessionException, ReceiveFailedException, DeletionFailedException, InterruptedException {
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
    void invalidSessionAfterLogout() throws LoginFailedException, RemoteException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        authenticationService.logout(sessionDTO.getSessionId());

        assertThrows(InvalidSessionException.class, () -> searchCustomerService.totResultsByFamilyName("Hanf", sessionDTO.getSessionId()));
    }

    @Test
    void allTopics() throws LoginFailedException, RemoteException, InvalidSessionException {
        List<String> expectedTopics = List.of("Pop", "Rock", "System.Message");

        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        List<String> actualTopics = messagingService.allTopics(sessionDTO.getSessionId());

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
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName("JUMP", 1);

        soundCarriers.forEach(sc -> assertTrue(sc.getAlbum().getSongs().stream().anyMatch(song -> song.getTitle().equals("JUMP"))));
    }

    @Test
    void soundCarrierByArticleId() {
        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("1000");

        assertEquals("1000", soundCarrier.getArticleId());
    }

    @Test
    void fillStock() {
        SoundCarrier soundCarrierBeforeFill = soundCarrierRepository.soundCarrierByArticleId("1000");
        int expectedStock = soundCarrierBeforeFill.getStock() + 1;

        soundCarrierRepository.fillStock("1000", 1);

        SoundCarrier soundCarrierAfterFill = soundCarrierRepository.soundCarrierByArticleId("1000");
        int actualStock = soundCarrierAfterFill.getStock();

        assertEquals(expectedStock, actualStock);
    }

    @Test
    void invoiceById() {
        Invoice invoice = invoiceRepository.invoiceById(20000L);

        assertEquals(20000L, invoice.getInvoiceId());
    }
}
