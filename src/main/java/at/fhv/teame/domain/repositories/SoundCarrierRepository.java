package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

public interface SoundCarrierRepository {

    void retrieveSoundCarriers(Map<SoundCarrier, Integer> soundCarriers) throws OutOfStockException, InvalidAmountException;

    SoundCarrier soundCarrierByArticleId(String articleId);

    List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr);

    List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr);

    List<SoundCarrier> soundCarriersBySongName(String song, int pageNr);

    Long nrOfRowsByAlbumName(String album);

    Long nrOfRowsByArtistName(String artist);

    Long nrOfRowsBySongName(String song);
}
