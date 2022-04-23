package at.fhv.teame.domain;

import at.fhv.teame.domain.model.soundcarrier.Song;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SongTest {

    //Song
    @Test
    void testSongConstructor() {
        //given
        String songTitle = "Perfect Strangers";
        LocalDate releaseDate = LocalDate.of(2010,1,1);
        String songDuration = "04:17";

        //when
        Song song = new Song("Perfect Strangers", LocalDate.of(2010,1,1), "04:17");

        //then
        assertEquals(songTitle, song.getTitle());
        assertEquals(releaseDate, song.getRelease());
        assertEquals(songDuration, song.getDuration());



    }


}
