package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;

import java.util.List;
import java.util.Map;

public interface SoundCarrierRepository {

    void processPurchase(Map<String, Integer> shoppingCartItems, String paymentMethod) throws OutOfStockException, InvalidAmountException;

    void fillStock(String articleId, int amount);

    List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr);

    List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr);

    List<SoundCarrier> soundCarriersBySongName(String song, int pageNr);

    Long totResultsByAlbumName(String album);

    Long totResultsByArtistName(String artist);

    Long totResultsBySongName(String song);

    SoundCarrier soundCarrierByArticleId(String articleId);
}
