package at.fhv.teame.integration;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

public class IntegrationTest {
    private SearchSoundCarrierServiceImpl soundCarrierService;

    @BeforeEach
    void setup() throws IOException {
        soundCarrierService = new SearchSoundCarrierServiceImpl();
    }

    @Test
    void soundCarriersByArtist() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.soundCarriersByArtistName("Cofer Brothers", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist() throws RemoteException {
        Integer nrOfSoundCarriers = soundCarrierService.totResultsByArtistName("AC/DC");
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum() throws RemoteException {
        Integer nrOfSoundCarriers = soundCarrierService.totResultsBySongName("Back In Black");
        System.out.println("count " + nrOfSoundCarriers);
    }



    @Test
    void getSoundCarriersByAlbum() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOS = soundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOS) {
            System.out.println(soundCarrierDTO.getAlbumName());
            System.out.println(soundCarrierDTO.getMedium());
        }
    }
}
