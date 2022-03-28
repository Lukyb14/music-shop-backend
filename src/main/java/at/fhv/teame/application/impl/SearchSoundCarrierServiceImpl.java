package at.fhv.teame.application.impl;

import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.SearchSoundCarrierService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SearchSoundCarrierServiceImpl extends UnicastRemoteObject implements SearchSoundCarrierService {
    private final SoundCarrierRepository soundCarrierRepository;
    private static final int ROWS_PER_PAGE = 10;
    private int iteratedSongs = 0;
    private int offset = 0;

    public SearchSoundCarrierServiceImpl() throws RemoteException {
        soundCarrierRepository = HibernateSoundCarrierRepository.getInstance();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album, pageNr);

        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersByAlbumName(album));
    }

    @Override
    public int numberOfSoundCarriersByAlbumName(String album) throws RemoteException {
        return soundCarrierRepository.nrOfRowsByAlbumName(album).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist, pageNr);

        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersByArtistName(artist));
    }

    @Override
    public int numberOfSoundCarriersByArtistName(String artist) throws RemoteException {
        return soundCarrierRepository.nrOfRowsByArtistName(artist).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersBySongName(String song, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName(song, pageNr);

        return buildSoundCarrierDtos(soundCarriers, pageNr, numberOfSoundCarriersBySongName(song));
    }

    @Override
    public int numberOfSoundCarriersBySongName(String song) throws RemoteException {
        return soundCarrierRepository.nrOfRowsBySongName(song).intValue();
    }

    private List<SoundCarrierDTO> buildSoundCarrierDtos(List<SoundCarrier> soundCarriers, int pageNr, int nrOfTotRowsInMatch) {
        List<SoundCarrierDTO> soundCarrierDtos = new LinkedList<>();

        for (SoundCarrier s : soundCarriers) {
            soundCarrierDtos.add(SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(),s.getMedium(), s.getPrice().toString(), s.getStock())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumLabel(),s.getAlbumGenre(), s.getAlbumArtist(), buildSongDtos(s.getAlbumSongs(), pageNr, nrOfTotRowsInMatch))
                    .build());
        }

        iteratedSongs = 0;

        return soundCarrierDtos;
    }

    private SongDTO[] buildSongDtos(List<Song> songs, int pageNr, int totResultsInMatch) {
        int maxResults;
        if((totResultsInMatch / (ROWS_PER_PAGE * pageNr)) > 0) {
            maxResults = ROWS_PER_PAGE;
        } else {
            maxResults = totResultsInMatch % ROWS_PER_PAGE;
        }

        int calculatedOffset = (pageNr - 1) * ROWS_PER_PAGE;

        List<SongDTO> songDtos = new LinkedList<>();


        for (Song s : songs) {
            if (offset == calculatedOffset) {
                System.out.println(pageNr);
                System.out.println("offset:"+offset);
                if(iteratedSongs != maxResults) {
                    iteratedSongs++;
                    SongDTO songDto = SongDTO.builder().withSongEntity(s.getTitle(), s.getRelease().toString()).build();
                    songDtos.add(songDto);
                }
            } else {
                offset++;
            }
        }

        SongDTO[] songDtosArr = new SongDTO[songDtos.size()];
        songDtosArr = songDtos.toArray(songDtosArr);

        return songDtosArr;
    }
}
