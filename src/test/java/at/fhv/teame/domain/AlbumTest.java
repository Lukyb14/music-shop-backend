package at.fhv.teame.domain;

import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Song;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumTest {

    @Test
    void testAlbumSongsAndReleaseDate() {
        //given
        List<Song> expectedSongs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "04:31");
        expectedSongs.add(song1);
        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                expectedSongs, "Rock", "TestArtist");

        //when...then
        assertEquals(LocalDate.of(1985,1,1), album.getRelease());
        assertEquals(expectedSongs, album.getSongs());
        assertEquals("Testname", album.getName());
        assertEquals("TestLabel", album.getLabel());
        assertEquals("Rock", album.getGenre());
        assertEquals("TestArtist", album.getArtist());
    }
}
