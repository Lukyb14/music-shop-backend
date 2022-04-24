package at.fhv.teame.integration;

import at.fhv.teame.application.impl.AuthenticationServiceImpl;
import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.AuthenticationService;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.rmi.exceptions.LoginFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class IntegrationTest {
    private SearchSoundCarrierServiceImpl soundCarrierService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() throws IOException {
        soundCarrierService = new SearchSoundCarrierServiceImpl();
        authenticationService = new AuthenticationServiceImpl();
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
}
