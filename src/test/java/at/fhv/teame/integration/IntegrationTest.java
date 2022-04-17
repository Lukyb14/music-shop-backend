package at.fhv.teame.integration;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public class IntegrationTest {
    private SearchSoundCarrierServiceImpl soundCarrierService;

    @BeforeEach
    void setup() throws IOException {
        soundCarrierService = new SearchSoundCarrierServiceImpl();
    }

    @Test
    void soundCarriersByArtist() throws RemoteException, InvalidSessionException {
        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.soundCarriersByArtistName("Cofer Brothers", 1, UUID.randomUUID().toString());

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist() throws RemoteException, InvalidSessionException {
        Integer nrOfSoundCarriers = soundCarrierService.totResultsByArtistName("AC/DC", UUID.randomUUID().toString());
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum() throws RemoteException, InvalidSessionException {
        Integer nrOfSoundCarriers = soundCarrierService.totResultsBySongName("Back In Black", UUID.randomUUID().toString());
        System.out.println("count " + nrOfSoundCarriers);
    }



    @Test
    void getSoundCarriersByAlbum() throws RemoteException, InvalidSessionException {
        List<SoundCarrierDTO> soundCarrierDTOS = soundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1, UUID.randomUUID().toString());

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOS) {
            System.out.println(soundCarrierDTO.getAlbumName());
            System.out.println(soundCarrierDTO.getMedium());
        }
    }
}
