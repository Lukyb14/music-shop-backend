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
    void testSoundCarrierConstructor () {
        //given
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                                LocalDate.of(1985,1,1),
                                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("1011", album, Medium.CD, new BigDecimal("31.69"), 10);

        //when...then
        assertEquals("1011", soundCarrier.getArticleId());
        assertEquals(album, soundCarrier.getAlbum());
        assertEquals(Medium.CD.toString(), soundCarrier.getMedium());
        assertEquals(new BigDecimal("31.69"), soundCarrier.getPrice());
        assertEquals(10, soundCarrier.getStock());
        assertEquals("Testname", soundCarrier.getAlbumName());
        assertEquals("TestLabel", soundCarrier.getAlbumLabel());
        assertEquals("TestArtist", soundCarrier.getAlbumArtist());
        assertEquals(songs, soundCarrier.getAlbumSongs());
        assertEquals("Rock", soundCarrier.getAlbumGenre());


    }

    @Test
    void given_article_out_of_stock_when_retrieve_then_out_of_stock_exception() throws OutOfStockException, InvalidAmountException {
        //given
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("295748", album, Medium.CD, new BigDecimal("31.69"), 0);

        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put("295748", 1);
        String paymentMethod = "cash";

        //when ... then
        assertThrows(OutOfStockException.class, () -> soundCarrier.retrieve(1));
    }

    @Test
    void given_invalid_amount_when_retrieve_then_invalid_amount_exception() {
        //given
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("295748", album, Medium.CD, new BigDecimal("31.69"), 10);

        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put("295748", -1);
        String paymentMethod = "cash";

        //when...then
        assertThrows(InvalidAmountException.class, () -> soundCarrier.retrieve(-1));
    }


//    @Test
//    void getTotalResultsByArtistName() {
//        //given
//        String artistName = "Diskobitch";
//        Long expectedTotResultsByArtistName = Long.valueOf(2);
//
//        //when
//        Long actualTotResultsByArtistName = soundCarrierRepository.totResultsByArtistName(artistName);
//
//        //then
//        assertEquals(actualTotResultsByArtistName, expectedTotResultsByArtistName);
//        assertNotNull(expectedTotResultsByArtistName);
//
//
//    }
//    @Test
//    void getTotalResultsBySongs() {
//        //given
//        String songName = "Los amos del desorden";
//        Long expectedTotResultsBySongName = Long.valueOf(2);
//
//        //when
//        Long actualTotResultsBySongName = soundCarrierRepository.totResultsBySongName(songName);
//
//        //then
//        assertEquals(actualTotResultsBySongName, expectedTotResultsBySongName);
//        assertNotNull(expectedTotResultsBySongName);
//
//
//    }
//
//    @Test
//    void getTotalResultsByAlbumName() {
//        //given
//        String albumName = "Love Me Crazy";
//        Long expectedTotResultsByAlbumName = Long.valueOf(2);
//
//        //when
//        Long actualTotResultsByAlbumName = soundCarrierRepository.totResultsByAlbumName(albumName);
//
//        //then
//        assertEquals(actualTotResultsByAlbumName, expectedTotResultsByAlbumName);
//        assertNotNull(expectedTotResultsByAlbumName);
//
//
//    }

    //SoundCarrier
      @Test
    void getSoundCarrierDetails() {
        //given
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("295748", album, Medium.CD, new BigDecimal("31.69"), 1);

        String expectedSoundCarrierArticleId = "295748";
        String expectedAlbum = "Testname";
        Medium expectedMedium = Medium.CD;
        String expectedAlbumLabel = "TestLabel";
        String expectedGenre = "Rock";
        String expectedArtistName = "TestArtist";
        BigDecimal expectedPrice = new BigDecimal("31.69").setScale(2, RoundingMode.HALF_UP);
        int expectedStock = 1;
        List<Song> expectedSongs = new ArrayList<>();
        expectedSongs.add(song1);

        //when
        String actualSoundCarrierArticleId = soundCarrier.getArticleId();
        String actualSoundCarrierAlbum = soundCarrier.getAlbum().getName();
        String actualAlbumName = soundCarrier.getAlbumName();
        BigDecimal actualPrice = soundCarrier.getPrice().setScale(2, RoundingMode.HALF_UP);
        String actualAlbumLabel = soundCarrier.getAlbumLabel();
        String actualGenre = soundCarrier.getAlbumGenre();
        String actualArtistName = soundCarrier.getAlbumArtist();
        Medium actualMedium = Medium.valueOf(soundCarrier.getMedium());
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


}
