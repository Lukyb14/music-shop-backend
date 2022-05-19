package at.fhv.teame.domain;

import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DigitalSongTest {

    @Test
    void testDigitalSongConstructor() {
        // given
        String artist = "Eminem";
        String songTitle = "8 Mile";
        String genre = "Hip-Hop";
        String duration = "5:30";
        LocalDate releaseDate = LocalDate.of(2000, 1, 1);
        String mp3File = "/songs/8 Mile.mp3";
        String coverFile = "/covers/8 Mile.jpg";

        // when
        DigitalSong digitalSong = new DigitalSong(
                "Eminem",
                "8 Mile",
                "Hip-Hop",
                "5:30",
                LocalDate.of(2000, 1, 1),
                "/songs/8 Mile.mp3",
                "/covers/8 Mile.jpg"
        );

        // then
        assertEquals(songTitle, digitalSong.getTitle());
        assertEquals(artist, digitalSong.getArtist());
        assertEquals(genre, digitalSong.getGenre());
        assertEquals(duration, digitalSong.getDuration());
        assertEquals(releaseDate, digitalSong.getReleaseDate());
        assertEquals(mp3File, digitalSong.getMp3File());
        assertEquals(coverFile, digitalSong.getCoverFile());
    }
}
