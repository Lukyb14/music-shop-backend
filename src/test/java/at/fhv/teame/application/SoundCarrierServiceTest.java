package at.fhv.teame.application;

import at.fhv.teame.application.impl.SoundCarrierServiceImpl;
import at.fhv.teame.domain.Album;
import at.fhv.teame.domain.Medium;
import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.SoundCarrierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoundCarrierServiceTest {

    private SoundCarrierService soundCarrierService;

    @BeforeEach
    void setup() throws IOException {
        soundCarrierService = SoundCarrierServiceImpl.getInstance();
    }

    @Test
    void getSoundCarriersByArtist() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.soundCarriersByArtistName("Sintellect", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOs) {
            System.out.println(soundCarrierDTO.albumName());
        }
    }

    @Test
    void getSoundCarriersByAlbum() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOS = soundCarrierService.soundCarriersByAlbumName("Beat Break #4", 1);

        for (SoundCarrierDTO soundCarrierDTO : soundCarrierDTOS) {
            System.out.println(soundCarrierDTO.albumName());
            System.out.println(soundCarrierDTO.medium());
        }
    }
}
