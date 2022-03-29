package at.fhv.teame.application.impl;

import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDetailsDTO;
import at.fhv.teame.sharedlib.rmi.SearchSoundCarrierService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SearchSoundCarrierServiceImpl extends UnicastRemoteObject implements SearchSoundCarrierService {
    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();

    public SearchSoundCarrierServiceImpl() throws RemoteException { super(); }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album, pageNr);
        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersByAlbumName(album));
    }

    @Override
    public int numberOfSoundCarriersByAlbumName(String album) throws RemoteException {
        return soundCarrierRepository.nrOfRowsByAlbumName(album).intValue();
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public int totResultsByAlbumName(String album) throws RemoteException {
        return soundCarrierRepository.totResultsByAlbumName(album).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist, pageNr);
        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersByArtistName(artist));
    }

    @Override
    public int numberOfSoundCarriersByArtistName(String artist) throws RemoteException {
        return soundCarrierRepository.nrOfRowsByArtistName(artist).intValue();
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public int totResultsByArtistName(String artist) throws RemoteException {
        return soundCarrierRepository.totResultsByArtistName(artist).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersBySongName(String song, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName(song, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }


        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersBySongName(song));
    }

    @Override
    public int numberOfSoundCarriersBySongName(String song) throws RemoteException {
        return soundCarrierRepository.nrOfRowsBySongName(song).intValue();
      

    @Override
    public int totResultsBySongName(String song) throws RemoteException {
        return soundCarrierRepository.totResultsBySongName(song).intValue();
    }

    @Override
    public SoundCarrierDetailsDTO soundCarrierDetailsByArticleId(String articleId) throws RemoteException {
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
            songDto[i] = SongDTO.builder().withSongEntity(s.getTitle(), s.getRelease().toString()).build();
        }
        return songDto;
    }
}
