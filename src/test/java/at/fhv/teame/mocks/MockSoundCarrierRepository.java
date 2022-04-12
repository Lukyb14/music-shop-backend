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
        return Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        return Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );
    }

    @Override
    public Long totResultsByAlbumName(String album) {return 2L;}

    @Override
    public Long totResultsByArtistName(String artist) {
        return 2L;
    }

    @Override
    public Long totResultsBySongName(String song) {
        return 2L;
    }

    @Override
    public SoundCarrier soundCarrierByArticleId(String articleId) {
        return createSoundCarrierDummy();
    }

    public SoundCarrier createSoundCarrierDummy() {
        return new SoundCarrier(
                String.valueOf(100),
                new Album("Black and White", "Black bars", LocalDate.of(2022,04,11), createSongListDummy(), "Pop", "Bob"),
                Medium.VINYL,
                BigDecimal.valueOf(20),
                3
        );
    }


    public List<Song> createSongListDummy() {
        return Arrays.asList(
                new Song("Hello World", LocalDate.of(2022, 04, 10), "2:42"),
                new Song("Apple M1", LocalDate.of(2022, 02, 10), "3:42"),
                new Song("Understruck", LocalDate.of(2022, 03, 10), "4:13")
        );
    }
}
