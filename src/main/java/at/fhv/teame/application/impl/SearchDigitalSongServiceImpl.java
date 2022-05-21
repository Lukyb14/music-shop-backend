package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;
import at.fhv.teame.sharedlib.dto.DigitalSongDTO;
import at.fhv.teame.sharedlib.ejb.SearchDigitalSongServiceRemote;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class SearchDigitalSongServiceImpl implements SearchDigitalSongServiceRemote {

    @EJB
    private DigitalSongRepository digitalSongRepository;

    public SearchDigitalSongServiceImpl() {}

    // for mocking
    public SearchDigitalSongServiceImpl(DigitalSongRepository digitalSongRepository) {
        this.digitalSongRepository = digitalSongRepository;
    }

    @Override
    public List<DigitalSongDTO> digitalSongByTitle(String title, int pageNr) {
        List<DigitalSong> digitalSongs = digitalSongRepository.digitalSongByTitle(title, pageNr);
        return buildDigitalSongDTOS(digitalSongs);
    }

    @Override
    public List<DigitalSongDTO> digitalSongByArtist(String artist, int pageNr) {
        List<DigitalSong> digitalSongs = digitalSongRepository.digitalSongByArtist(artist, pageNr);
        return buildDigitalSongDTOS(digitalSongs);
    }

    @Override
    public List<DigitalSongDTO> digitalSongByGenre(String genre, int pageNr) {
        List<DigitalSong> digitalSongs = digitalSongRepository.digitalSongByGenre(genre, pageNr);
        return buildDigitalSongDTOS(digitalSongs);
    }

    private List<DigitalSongDTO> buildDigitalSongDTOS (List<DigitalSong> digitalSongs) {
        List<DigitalSongDTO> digitalSongDTOS = new ArrayList<>();
        for (DigitalSong ds : digitalSongs) {
            digitalSongDTOS.add(
                    DigitalSongDTO.builder()
                            .withDigitalSongEntity(ds.getArtist(), ds.getTitle(), ds.getGenre(), ds.getDuration(), ds.getReleaseDate().toString())
                            .build()
            );
        }
        return digitalSongDTOS;
    }
}
