package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DigitalSongRepository {
    DigitalSong digitalSongByArticleId(Long articleId);
    List<DigitalSong> digitalSongByTitle(String title, int pageNr);
    List<DigitalSong> digitalSongByArtist(String artist, int pageNr);
    List<DigitalSong> digitalSongByGenre(String genre, int pageNr);

}
