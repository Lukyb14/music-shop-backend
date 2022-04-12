package at.fhv.teame.mocks;

import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MockSoundCarrierRepository implements SoundCarrierRepository {

    static int nextArticleId = 10000;


    @Override
    public void processPurchase(Map<String, Integer> shoppingCartItems) throws OutOfStockException, InvalidAmountException {

    }

    @Override
    public void fillStock(String articleId, int amount) {

    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr) {
        return Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        return null;
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        return null;
    }

    @Override
    public Long totResultsByAlbumName(String album) {
        return null;
    }

    @Override
    public Long totResultsByArtistName(String artist) {
        return null;
    }

    @Override
    public Long totResultsBySongName(String song) {
        return null;
    }

    @Override
    public SoundCarrier soundCarrierByArticleId(String articleId) {
        return createSoundCarrierDummy();
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
