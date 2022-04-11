package at.fhv.teame.application.impl;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchSoundCarrierServiceTest {

    static SearchSoundCarrierServiceImpl searchSoundCarrierService;

    static int nextArticleId = 10000;

    @BeforeAll
    static void beforeAll() throws RemoteException {
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl(new MockSoundCarrierRepository());
    }

    @Test
    void given_2soundCarriersinrepository_when_soundCarriersByAlbumName_then_expectsoundCarrierssequals() throws RemoteException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        List<SoundCarrierDTO> soundCarrierDtosExpected = new ArrayList<>();
        for (SoundCarrier s : soundCarriers) {
            SoundCarrierDTO soundCarrierDTO = SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock(), s.getAlbumSongs().size())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumArtist(), s.getAlbumGenre())
                    .build();
            soundCarrierDtosExpected.add(soundCarrierDTO);
        }

        //when
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersByAlbumName("Black and White", 1);

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }


    public SoundCarrier createSoundCarrierDummy() {
        return new SoundCarrier(
                String.valueOf(nextArticleId++),
                new Album("Black and White", "Black bars", LocalDate.of(2022,04,11), createSongListDummy(), "Pop", "Bob"),
                Medium.VINYL,
                BigDecimal.valueOf(20),
                3
        );
    }


    public List<Song> createSongListDummy() {
        return Arrays.asList(
                new Song("Hello World", LocalDate.of(2022, 04, 10)),
                new Song("Apple M1", LocalDate.of(2022, 02, 10)),
                new Song("Understruck", LocalDate.of(2022, 03, 10))
        );
    }
}
