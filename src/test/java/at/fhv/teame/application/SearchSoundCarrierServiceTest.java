package at.fhv.teame.application;

import at.fhv.teame.application.impl.SearchSoundCarrierImplementation;
import at.fhv.teame.domain.Album;
import at.fhv.teame.domain.Medium;
import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchSoundCarrierServiceTest {

    private SoundCarrierRepository soundCarrierRepository;

    private SearchSoundCarrierService searchSoundCarrierService;

    private List<SoundCarrier> soundCarriers;

    @BeforeEach
    void setup() {
        searchSoundCarrierService = new SearchSoundCarrierImplementation();

        List<Song> songs = List.of(new Song("Fear Is the Key", "Iron Maiden", LocalDate.of(1992, 1, 1)));
        Album album = new Album("Fear of the Dark", "Parlophone Records Ltd", LocalDate.of(1992, 1, 1), songs, "Rock");
        soundCarriers = List.of(new SoundCarrier(album, Medium.CD, new BigDecimal("35.99"), 3));
    }

    @Test
    void testGetAllSoundCarriers() {
        List<SoundCarrierDTO> soundCarrierDTOs = searchSoundCarrierService.getAllSoundCarriers();

        assertEquals(soundCarriers.size(), soundCarrierDTOs.size());

        for (int i = 0; i < soundCarriers.size(); i++) {
            assertEquals(soundCarriers.get(i).getAlbum().getName(), soundCarrierDTOs.get(i).albumName());
            assertEquals(soundCarriers.get(i).getPrice().toString(), soundCarrierDTOs.get(i).price());
            assertEquals(soundCarriers.get(i).getMedium().toString(), soundCarrierDTOs.get(i).medium());
            assertEquals(soundCarriers.get(i).getStock(), soundCarrierDTOs.get(i).stock());
            for (int j = 0; j < soundCarriers.get(i).getAlbum().getSongs().size(); j++) {
                assertEquals(soundCarriers.get(i).getAlbum().getSongs().get(j).getTitle(), soundCarrierDTOs.get(i).songs()[j].songName());
                assertEquals(soundCarriers.get(i).getAlbum().getSongs().get(j).getArtist(), soundCarrierDTOs.get(i).songs()[j].artist());
                assertEquals(soundCarriers.get(i).getAlbum().getSongs().get(j).getRelease().toString(), soundCarrierDTOs.get(i).songs()[j].release());
            }
        }
    }
}
