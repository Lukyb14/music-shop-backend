package at.fhv.teame.application;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.SearchSoundCarrierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoundCarrierServiceTest {

    private SearchSoundCarrierService soundCarrierService;

    @BeforeEach
    void setup() throws IOException {
        soundCarrierService = new SearchSoundCarrierServiceImpl();
    }

    @Test
    void soundCarriersByArtist() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.soundCarriersByArtistName("Sintellect", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.getAlbumName());
        }
    }

    @Test
    void nrOfSongsByArtist() throws RemoteException {
        Integer nrOfSoundCarriers = soundCarrierService.numberOfSoundCarriersByArtistName("AC/DC");
        System.out.println("count " + nrOfSoundCarriers);
    }

    @Test
    void nrOfSongsByAlbum() throws RemoteException {
        Integer nrOfSoundCarriers = soundCarrierService.numberOfSoundCarriersByAlbumName("Back In Black");
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
