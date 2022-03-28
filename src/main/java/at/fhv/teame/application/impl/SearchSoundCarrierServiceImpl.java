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
    private boolean filledPage = false;
    private int i = 0;
    private int j = 0;

    public SearchSoundCarrierServiceImpl() throws RemoteException {
        soundCarrierRepository = HibernateSoundCarrierRepository.getInstance();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album, pageNr);
        return buildSoundCarrierDtos(soundCarriers, pageNr, nrOfRowsByAlbumName(album));
    }

    @Override
    public int nrOfRowsByAlbumName(String album) throws RemoteException {
        return soundCarrierRepository.totResultsByAlbumName(album).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist, pageNr);
        return buildSoundCarrierDtos(soundCarriers, pageNr, nrOfRowsByArtistName(artist));
    }

    @Override
    public int nrOfRowsByArtistName(String artist) throws RemoteException {
        return soundCarrierRepository.totResultsByArtistName(artist).intValue();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersBySongName(String song, int pageNr) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName(song, pageNr);
        return buildSoundCarrierDtos(soundCarriers, pageNr, nrOfRowsBySongName(song));
    }

    @Override
    public int nrOfRowsBySongName(String song) throws RemoteException {
        return soundCarrierRepository.totResultsBySongName(song).intValue();
    }

    private List<SoundCarrierDTO> buildSoundCarrierDtos(List<SoundCarrier> soundCarriers, int pageNr, int nrOfTotRowsInMatch) {
        List<SoundCarrierDTO> soundCarrierDtos = new LinkedList<>();
        for (SoundCarrier s : soundCarriers) {
            if(!filledPage) {
                soundCarrierDtos.add(SoundCarrierDTO.builder()
                        .withSoundCarrierEntity(s.getArticleId(),s.getMedium(), s.getPrice().toString(), s.getStock())
                        .withAlbumEntity(s.getAlbumName(), s.getAlbumLabel(),s.getAlbumGenre(), s.getAlbumArtist(), buildSongDtos(s.getAlbumSongs(), pageNr, nrOfTotRowsInMatch))
                        .build());
            }

        }
        filledPage = false;
        return soundCarrierDtos;
    }

    private SongDTO[] buildSongDtos(List<Song> songs, int pageNr, int totResultsInMatch) {
        List<SongDTO> songDtos = new LinkedList<>();
        int calculatedOffset = (pageNr - 1) * ROWS_PER_PAGE;
        int maxResults = calcMaxResults(totResultsInMatch, pageNr);
        System.out.println("pageNr: " + pageNr);
        System.out.println("calculatedOffset: " + calculatedOffset);
        System.out.println("maxResults: " + maxResults);

        if(pageNr == 1) {
            i = 0;
            j = 0;
        }

        //1. Gehe durch alle songs durch
        //2. überspringe songs bis zum calculatedOffset
        //3. Baue songs bis zum maxResult
        for (Song s : songs) { // 1.
            if(j == calculatedOffset) { //2.
                if (i < maxResults) { //3.
                    SongDTO songDto = SongDTO.builder().withSongEntity(s.getTitle(), s.getRelease().toString()).build();
                    songDtos.add(songDto);
                } else {
                    //maxResult per page reached
                    i = 0;
                    j = 0;
                    filledPage = true;
                    break;
                }
                i++;
            } else {
                j++;
            }
        }

        SongDTO[] songDtosArr = new SongDTO[songDtos.size()];
        songDtosArr = songDtos.toArray(songDtosArr);
        return songDtosArr;
    }

    private int calcMaxResults(int totResultsInMatch, int pageNr) {
        int maxResults;
        if((totResultsInMatch / (ROWS_PER_PAGE * pageNr)) > 0) {
            maxResults = ROWS_PER_PAGE;
        } else {
            maxResults = totResultsInMatch % ROWS_PER_PAGE;
        }
        return maxResults;
    }
}
