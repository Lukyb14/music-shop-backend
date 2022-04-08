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
import java.util.List;
import java.util.Map;

public class MockSoundCarrierRepository implements SoundCarrierRepository {
    @Override
    public void processPurchase(Map<String, Integer> shoppingCartItems, String paymentMethod) throws OutOfStockException, InvalidAmountException {

    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr) {
        return List.of(
                new SoundCarrier(
                        "1000",
                        new Album(
                                "zachary",
                                "label",
                                LocalDate.of(2022, 1, 1),
                                List.of(new Song(
                                        "songname",
                                        LocalDate.of(2022, 1, 1))),
                                "genre",
                                "artist"
                        ),
                        Medium.CD,
                        BigDecimal.valueOf(12.99),
                        3)
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
        return null;
    }
}
