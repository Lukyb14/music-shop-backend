package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.onlineshop.DigitalSong;

import java.util.List;

public interface DigitalSongRepository {
    List<DigitalSong> digitalSongByTitle(String title, int pageNr);
    List<DigitalSong> digitalSongByArtist(String artist, int pageNr);
    List<DigitalSong> digitalSongByGenre(String genre, int pageNr);

}
