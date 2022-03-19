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

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoundCarrierServiceTest {

    private SoundCarrierRepository soundCarrierRepository;
    private SoundCarrierService soundCarrierService;

    private List<SoundCarrier> soundCarriers;

    @BeforeEach
    void setup() throws RemoteException {
        soundCarrierService = SoundCarrierServiceImpl.getInstance();

        List<Song> songs = List.of(new Song("Fear Is the Key", "Iron Maiden", LocalDate.of(1992, 1, 1)));
        Album album = new Album("Fear of the Dark", "Parlophone Records Ltd", LocalDate.of(1992, 1, 1), songs,"Rock");
        soundCarriers = List.of(new SoundCarrier(album, Medium.CD, new BigDecimal("35.99"), 3));
    }

    @Test
    void testGetAllSoundCarriers() throws RemoteException {
        List<SoundCarrierDTO> soundCarrierDTOs = soundCarrierService.allSoundCarriers();

        assertEquals(3, soundCarrierDTOs.size());

//        for (int i = 0; i < soundCarriers.size(); i++) {
//            assertEquals(soundCarriers.get(i).getAlbum().getName(), soundCarrierDTOs.get(i).albumName());
//            assertEquals(soundCarriers.get(i).getPrice().toString(), soundCarrierDTOs.get(i).price());
//            assertEquals(soundCarriers.get(i).getMedium().toString(), soundCarrierDTOs.get(i).medium());
//            assertEquals(soundCarriers.get(i).getStock(), soundCarrierDTOs.get(i).stock());
//        }
    }
}
