package at.fhv.teame.domain;

import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SoundCarrierTest {



    private HibernateSoundCarrierRepository soundCarrierRepository;


    @BeforeEach
    void setup()  {

        soundCarrierRepository = new HibernateSoundCarrierRepository();
    }


    @Test
    void outOfStockException()  {
        //given
        String articleId = "1014";
        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put(articleId, 1);
        String paymentMethod = "cash";

        //when...then
        assertThrows(OutOfStockException.class, () -> soundCarrierRepository.processPurchase(purchasedItems));
    }

    @Test
    void invalidAmountException() {
        //given
        String articleId = "1023";
        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put(articleId, -1);
        String paymentMethod = "cash";

        //when...then
        assertThrows(InvalidAmountException.class, () -> soundCarrierRepository.processPurchase(purchasedItems));
    }



    @Test
    void getTotalResultsByArtistName() {
        //given
        String artistName = "Diskobitch";
        Long expectedTotResultsByArtistName = Long.valueOf(2);

        //when
        Long actualTotResultsByArtistName = soundCarrierRepository.totResultsByArtistName(artistName);

        //then
        assertEquals(actualTotResultsByArtistName, expectedTotResultsByArtistName);
        assertNotNull(expectedTotResultsByArtistName);


    }
    @Test
    void getTotalResultsBySongs() {
        //given
        String songName = "Los amos del desorden";
        Long expectedTotResultsBySongName = Long.valueOf(2);

        //when
        Long actualTotResultsBySongName = soundCarrierRepository.totResultsBySongName(songName);

        //then
        assertEquals(actualTotResultsBySongName, expectedTotResultsBySongName);
        assertNotNull(expectedTotResultsBySongName);


    }

    @Test
    void getTotalResultsByAlbumName() {
        //given
        String albumName = "Love Me Crazy";
        Long expectedTotResultsByAlbumName = Long.valueOf(2);

        //when
        Long actualTotResultsByAlbumName = soundCarrierRepository.totResultsByAlbumName(albumName);

        //then
        assertEquals(actualTotResultsByAlbumName, expectedTotResultsByAlbumName);
        assertNotNull(expectedTotResultsByAlbumName);


    }


    //SoundCarrier
    @Test
    void getSoundCarrierDetails() {
        //given
        String expectedSoundCarrierArticleId = "1015";
        String expectedAlbum = "Brothers In Arms";
        String expectedMedium = "VINYL";
        String expectedAlbumLabel = "Mercury Records Limited";
        String expectedGenre = "Rock";
        String expectedArtistName = "Dire Straits";
        BigDecimal expectedPrice = new BigDecimal(5.99).setScale(2, RoundingMode.HALF_UP);
        int expectedStock = 1;
        List<Song> expectedSongs = new ArrayList<>();
        Song song1 = new Song("Money For Nothing", LocalDate.of(1985, 1, 1), "03:41");
        Song song2 = new Song("Walk of Life", LocalDate.of(1985,1,1), "03:21");
        Song song3 = new Song("Brothers in Arms", LocalDate.of(1985,1,1), "04:22");
        expectedSongs.add(song1);
        expectedSongs.add(song2);
        expectedSongs.add(song3);

        //when
        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(expectedSoundCarrierArticleId);
        String actualSoundCarrierArticleId = soundCarrier.getArticleId();
        String actualSoundCarrierAlbum = soundCarrier.getAlbum().getName();
        String actualAlbumName = soundCarrier.getAlbumName();
        BigDecimal actualPrice = soundCarrier.getPrice().setScale(2, RoundingMode.HALF_UP);
        String actualAlbumLabel = soundCarrier.getAlbumLabel();
        String actualGenre = soundCarrier.getAlbumGenre();
        String actualArtistName = soundCarrier.getAlbumArtist();
        String actualMedium = soundCarrier.getMedium();
        int actualStock = soundCarrier.getStock();
        List<Song> actualSongs = new ArrayList<>();
        //actualSongs.add(soundCarrier.getAlbumSongs().get(0).getId(7));


        //then
        assertEquals(actualSoundCarrierArticleId, actualSoundCarrierArticleId);
        assertEquals(actualSoundCarrierAlbum, expectedAlbum);
        assertEquals(actualAlbumName, expectedAlbum);
        assertEquals(expectedMedium, actualMedium);
        assertEquals(actualStock, expectedStock);
        assertEquals(actualAlbumLabel, expectedAlbumLabel);
        assertEquals(expectedGenre, actualGenre);
        assertEquals(expectedArtistName, actualArtistName);
        assertEquals(actualPrice, expectedPrice);
        //assertEquals(expectedSongs, actualSongs);

    }



    @Test
    void testSoundCarrierConstructor () {
        //given
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                                LocalDate.of(1985,1,1),
                                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("1011", album, Medium.CD, new BigDecimal(31.69), 10);

        //when...then
        assertEquals("1011", soundCarrier.getArticleId());
        assertEquals(album, soundCarrier.getAlbum());
        assertEquals(Medium.CD.toString(), soundCarrier.getMedium());
        assertEquals(new BigDecimal(31.69), soundCarrier.getPrice());
        assertEquals(10, soundCarrier.getStock());



    }

    //Album
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
    }

    //Song
    @Test
    void testSongConstructor() {
        //given
        String songTitle = "Perfect Strangers";
        LocalDate releaseDate = LocalDate.of(2010,1,1);

        //when
        Song song = new Song("Perfect Strangers", LocalDate.of(2010,1,1), "04:17");

        //then
        assertEquals(songTitle, song.getTitle());
        assertEquals(releaseDate, song.getRelease());

    }
}
