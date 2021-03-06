package at.fhv.teame.application.impl;

import at.fhv.teame.application.impl.soundcarrier.SearchSoundCarrierServiceImpl;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDetailsDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchSoundCarrierServiceTest {

    private SearchSoundCarrierServiceRemote searchSoundCarrierService;


    @BeforeEach
    void beforeEach() {
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl(new MockSoundCarrierRepository());
    }

    @Test
    void given_2soundcarriersinrepository_when_soundcarriersByAlbumName_then_expectsoundcarrierssequals() {
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


    @Test
    void given_totResultsExpected_when_totResultsByAlbumName_then_totResultsequals() {
        //given
        int totResultsExpected = 2;

        //when
        int totResultsActual = searchSoundCarrierService.totResultsByAlbumName("Black and White");

        //then
        assertEquals(totResultsExpected, totResultsActual);
    }


    @Test
    void given_2soundcarriersinrepository_when_soundcarriersByArtistName_then_expectsoundscarriersequals() {
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
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersByArtistName("Bob", 1);

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }

    @Test
    void given_totResultsExpected_when_totResultsByArtistName_then_totResultsequals() {
        //given
        int totResultsExpected = 2;

        //when
        int totResultsActual = searchSoundCarrierService.totResultsByArtistName("Bob");

        //then
        assertEquals(totResultsExpected, totResultsActual);
    }

    @Test
    void given_2soundcarriersinrepository_when_soundcarriersBySongName_then_expectsoundscarriersequals() {
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
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersBySongName("Apple M1", 1);

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }

    @Test
    void given_totResultsExpected_when_totResultsBySongName_then_totResultsequals() {
        //given
        int totResultsExpected = 2;

        //when
        int totResultsActual = searchSoundCarrierService.totResultsBySongName("Hello World");

        //then
        assertEquals(totResultsExpected, totResultsActual);
    }

    @Test
    void given_soundcarrierinrepository_when_soundcarrierdetailsByArticleId_then_detailsequals() {
        //given
        SoundCarrier s = createSoundCarrierDummy();

        SoundCarrierDetailsDTO soundCarrierDetailsDTOExpected = SoundCarrierDetailsDTO.builder()
                .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock())
                .withAlbumEntity(s.getAlbumName(), s.getAlbumLabel(), s.getAlbumGenre(), s.getAlbumArtist(), buildSongDtos(s.getAlbumSongs()))
                .build();

        //when
        SoundCarrierDetailsDTO soundCarrierDetailsDTOActual = searchSoundCarrierService.soundCarrierDetailsByArticleId("100");

        //then
        assertEquals(soundCarrierDetailsDTOExpected, soundCarrierDetailsDTOActual);
    }

    private SoundCarrier createSoundCarrierDummy() {
        return new SoundCarrier(
                String.valueOf(100),
                new Album("Black and White", "Black bars", LocalDate.of(2022, 04, 11), createSongListDummy(), "Pop", "Bob"),
                Medium.VINYL,
                BigDecimal.valueOf(20),
                3
        );
    }

    private List<Song> createSongListDummy() {
        return Arrays.asList(
                new Song("Hello World", LocalDate.of(2022, 04, 10), "3:10"),
                new Song("Apple M1", LocalDate.of(2022, 02, 10), "2:54"),
                new Song("Understruck", LocalDate.of(2022, 03, 10), "4:03")
        );
    }

    private SongDTO[] buildSongDtos(List<Song> songs) {
        SongDTO[] songDto = new SongDTO[songs.size()];
        for (int i = 0; i < songDto.length; i++) {
            Song s = songs.get(i);
            songDto[i] = SongDTO.builder().withSongEntity(s.getTitle(), s.getRelease().toString(), s.getDuration()).build();
        }
        return songDto;
    }
}
