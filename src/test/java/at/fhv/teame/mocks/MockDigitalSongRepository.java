package at.fhv.teame.mocks;

import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockDigitalSongRepository implements DigitalSongRepository {
    @Override
    public DigitalSong digitalSongByArticleId(Long articleId) {
        return null;
    }

    @Override
    public List<DigitalSong> digitalSongByTitle(String title, int pageNr, int pageSize) {
        return createDigitalSongLstDummy();
    }

    @Override
    public List<DigitalSong> digitalSongByArtist(String artist, int pageNr, int pageSize) {
        return createDigitalSongLstDummy();
    }

    @Override
    public List<DigitalSong> digitalSongByGenre(String genre, int pageNr, int pageSize) {
        return createDigitalSongLstDummy();
    }

    @Override
    public Long totResultsByTitle(String title) {
        return null;
    }

    @Override
    public Long totResultsByArtistName(String artist) {
        return null;
    }

    @Override
    public Long totResultsByGenre(String genre) {
        return null;
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
