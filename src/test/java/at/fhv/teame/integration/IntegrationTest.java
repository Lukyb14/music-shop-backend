package at.fhv.teame.integration;

import at.fhv.teame.application.impl.*;
import at.fhv.teame.sharedlib.dto.*;
import at.fhv.teame.sharedlib.rmi.*;
import at.fhv.teame.sharedlib.rmi.exceptions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    private static AuthenticationService authenticationService;
    private static MessageService messagingService;
    private static PurchaseSoundCarrierService purchaseSoundCarrierService;
    private static SearchCustomerService searchCustomerService;
    private static SearchInvoiceService searchInvoiceService;
    private static SearchSoundCarrierServiceImpl soundCarrierService;
    private static WithdrawSoundCarrierService withdrawSoundCarrierService;

    @BeforeAll
    static void setup() throws IOException {
        authenticationService = new AuthenticationServiceImpl();
        messagingService = new MessagingServiceImpl();
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl();
        searchCustomerService = new SearchCustomerServiceImpl();
        searchInvoiceService = new SearchInvoiceServiceImpl();
        soundCarrierService = new SearchSoundCarrierServiceImpl();
        withdrawSoundCarrierService = new WithdrawalSoundCarrierServiceImpl();
    }

    @Test
    void soundCarriersByArtist() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.soundCarriersByArtistName("Cofer Brothers", 1, sessionDTO.getSessionId());

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        Integer nrOfSoundCarriers = soundCarrierService.totResultsByArtistName("AC/DC", sessionDTO.getSessionId());
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        Integer nrOfSoundCarriers = soundCarrierService.totResultsBySongName("Back In Black", sessionDTO.getSessionId());
        System.out.println("count " + nrOfSoundCarriers);
    }



    @Test
    void getSoundCarriersByAlbum() throws RemoteException, InvalidSessionException, LoginFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<SoundCarrierDTO> soundCarrierDTOS = soundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1, sessionDTO.getSessionId());

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
    void publishMessage() throws LoginFailedException, RemoteException, InvalidSessionException, ReceiveFailedException, PublishingFailedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        List<MessageDTO> messagesBefore = messagingService.fetchMessages(sessionDTO.getSessionId());

        messagingService.publishMessage(new MessageDTO("Rock", "New Concert", "Testing publishMessage()"), sessionDTO.getSessionId());

        List<MessageDTO> messagesAfter = messagingService.fetchMessages(sessionDTO.getSessionId());

        assertTrue(messagesBefore.size() < messagesAfter.size());
    }

    @Test
    void deleteMessage() throws LoginFailedException, RemoteException, InvalidSessionException, ReceiveFailedException, DeletionFailedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        List<MessageDTO> messagesBefore = messagingService.fetchMessages(sessionDTO.getSessionId());

        messagingService.deleteMessage(messagesBefore.get(0).getId(), sessionDTO.getSessionId());

        List<MessageDTO> messagesAfter = messagingService.fetchMessages(sessionDTO.getSessionId());

        assertTrue(messagesBefore.size() > messagesAfter.size());
    }
}
