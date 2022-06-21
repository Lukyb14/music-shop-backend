package at.fhv.teame.application.impl;

import at.fhv.teame.application.impl.digitalsong.SearchDigitalSongServiceImpl;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.mocks.MockDigitalSongRepository;
import at.fhv.teame.sharedlib.dto.DigitalSongDTO;
import at.fhv.teame.sharedlib.ejb.SearchDigitalSongServiceRemote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchDigitalSongServiceTest {
    private SearchDigitalSongServiceRemote searchDigitalSongService;

    @BeforeEach
    void beforeEach() {
        searchDigitalSongService = new SearchDigitalSongServiceImpl(new MockDigitalSongRepository());
    }

    @Test
    void given_song_title_when_search_digital_song_by_artist_then_expected_song_equals() {
        // given
        List<DigitalSong> digitalSongs = createDigitalSongLstDummy();

        List<DigitalSongDTO> digitalSongDtosExpected = new ArrayList<>();
        for (DigitalSong ds : digitalSongs) {
            DigitalSongDTO digitalSongDTO = DigitalSongDTO.builder()
                    .withDigitalSongEntity(ds.getArtist(), ds.getTitle(), ds.getGenre(), ds.getDuration(), ds.getReleaseDate().toString(), ds.getPrice().toString(), "1")
                    .build();
            digitalSongDtosExpected.add(digitalSongDTO);
        }
        // when
        List<DigitalSongDTO> digitalSongDtosActual = searchDigitalSongService.digitalSongByArtist("Eminem", 1, 10);

        // then
        for (DigitalSongDTO ds : digitalSongDtosActual) {
            assertTrue(digitalSongDtosExpected.contains(ds));
        }
    }

    @Test
    void given_song_title_when_search_digital_song_by_genre_then_expected_song_equals() {
        // given
        List<DigitalSong> digitalSongs = createDigitalSongLstDummy();

        List<DigitalSongDTO> digitalSongDtosExpected = new ArrayList<>();
        for (DigitalSong ds : digitalSongs) {
            DigitalSongDTO digitalSongDTO = DigitalSongDTO.builder()
                    .withDigitalSongEntity(ds.getArtist(), ds.getTitle(), ds.getGenre(), ds.getDuration(), ds.getReleaseDate().toString(), ds.getPrice().toString(), "1")
                    .build();
            digitalSongDtosExpected.add(digitalSongDTO);
        }
        // when
        List<DigitalSongDTO> digitalSongDtosActual = searchDigitalSongService.digitalSongByGenre("Hip-Hop",1, 10);

        // then
        for (DigitalSongDTO ds : digitalSongDtosActual) {
            assertTrue(digitalSongDtosExpected.contains(ds));
        }
    }

    @Test
    void given_song_title_when_search_digital_song_by_title_then_expected_song_equals() {
        // given
        List<DigitalSong> digitalSongs = createDigitalSongLstDummy();

        List<DigitalSongDTO> digitalSongDtosExpected = new ArrayList<>();
        for (DigitalSong ds : digitalSongs) {
            DigitalSongDTO digitalSongDTO = DigitalSongDTO.builder()
                    .withDigitalSongEntity(ds.getArtist(), ds.getTitle(), ds.getGenre(), ds.getDuration(), ds.getReleaseDate().toString(), ds.getPrice().toString(), String.valueOf(ds.getId()))
                    .build();
            digitalSongDtosExpected.add(digitalSongDTO);
        }
        // when
        List<DigitalSongDTO> digitalSongDtosActual = searchDigitalSongService.digitalSongByTitle("8 Mile", 1, 10);

        // then
        for (DigitalSongDTO ds : digitalSongDtosActual) {
            assertTrue(digitalSongDtosExpected.contains(ds));
        }
    }

    public List<DigitalSong> createDigitalSongLstDummy() {
        return Arrays.asList(
                new DigitalSong(1L,
                        "Eminem",
                        "8 Mile",
                        "Hip-Hop",
                        "5:30",
                        LocalDate.of(2000, 1, 1),
                        "/songs/8 Mile.mp3",
                        "/covers/8 Mile.jpg",
                        BigDecimal.valueOf(2.99f)
                ),
                new DigitalSong(1L,
                        "Eminem",
                        "Not Afraid",
                        "Hip-Hop",
                        "6:30",
                        LocalDate.of(2011, 3, 1),
                        "/songs/Not Afraid.mp3",
                        "/covers/Not Afraid.jpg",
                        BigDecimal.valueOf(1.99f)
                ),new DigitalSong(1L,
                        "Eminem",
                        "Till I Collapse",
                        "Hip-Hop",
                        "5:50",
                        LocalDate.of(2003, 4, 1),
                        "/songs/Till I Collapse.mp3",
                        "/covers/Till I Collapse.jpg",
                        BigDecimal.valueOf(3.99f)
                )
        );
    }
}
