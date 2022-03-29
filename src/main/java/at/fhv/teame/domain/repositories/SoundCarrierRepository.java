package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;

import java.util.List;
import java.util.Map;

public interface SoundCarrierRepository {

    void processPurchase(Map<String, Integer> shoppingCartItems, String paymentMethod) throws OutOfStockException, InvalidAmountException;

    List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr);

    List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr);

    List<SoundCarrier> soundCarriersBySongName(String song, int pageNr);

    Long nrOfRowsByAlbumName(String album);

    Long nrOfRowsByArtistName(String artist);

    Long nrOfRowsBySongName(String song);
}
