package at.fhv.teame.application.impl.ejb;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.connection.Session;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDetailsDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;

import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Stateless
public class SearchSoundCarrierServiceEJB implements SearchSoundCarrierServiceRemote {
    private final SoundCarrierRepository soundCarrierRepository;
    private final SessionRepository sessionRepository;

    public SearchSoundCarrierServiceEJB() {
        this(new HibernateSoundCarrierRepository(), new ListSessionRepository());
    }

    public SearchSoundCarrierServiceEJB(SoundCarrierRepository soundCarrierRepository, SessionRepository sessionRepository) {
        this.soundCarrierRepository = soundCarrierRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album, int pageNr, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist, int pageNr, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersBySongName(String song, int pageNr, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName(song, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public int totResultsByAlbumName(String album, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return soundCarrierRepository.totResultsByAlbumName(album).intValue();
    }

    @Override
    public int totResultsByArtistName(String artist, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return soundCarrierRepository.totResultsByArtistName(artist).intValue();
    }

    @Override
    public int totResultsBySongName(String song, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return soundCarrierRepository.totResultsBySongName(song).intValue();
    }

    @Override
    public SoundCarrierDetailsDTO soundCarrierDetailsByArticleId(String articleId, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(articleId);
        return buildSoundCarrierDetailsDto(soundCarrier);
    }

    private List<SoundCarrierDTO> buildSoundCarrierDtos(List<SoundCarrier> soundCarriers) {
        List<SoundCarrierDTO> soundCarrierDtos = new LinkedList<>();
        for (SoundCarrier s : soundCarriers) {
            soundCarrierDtos.add(SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock(), s.getAlbumSongs().size())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumArtist(), s.getAlbumGenre())
                    .build());
        }
        return soundCarrierDtos;
    }

    private SoundCarrierDetailsDTO buildSoundCarrierDetailsDto(SoundCarrier s) {
        return SoundCarrierDetailsDTO.builder()
                .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock())
                .withAlbumEntity(s.getAlbumName(), s.getAlbumLabel(), s.getAlbumGenre(), s.getAlbumArtist(), buildSongDtos(s.getAlbumSongs()))
                .build();
    }

    private SongDTO[] buildSongDtos(List<Song> songs) {
        SongDTO[] songDto = new SongDTO[songs.size()];

        for (int i = 0; i < songDto.length; i++) {
            Song s = songs.get(i);
            songDto[i] = SongDTO.builder().withSongEntity(s.getTitle(), s.getRelease().toString(), s.getDuration()).build();
        }
        return songDto;
    }
}
