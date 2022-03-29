package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SoundCarrierRepository {
    List<SoundCarrier> soundCarriersByAlbumName(String album, int pageNr);

    List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr);

    List<SoundCarrier> soundCarriersBySongName(String song, int pageNr);

    Long totResultsByAlbumName(String album);

    Long totResultsByArtistName(String artist);

    Long totResultsBySongName(String song);

    SoundCarrier soundCarrierByArticleId(String articleId);
}
