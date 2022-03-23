package at.fhv.teame.application.impl;

import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.rmi.SoundCarrierService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.List;

public class SoundCarrierServiceImpl extends UnicastRemoteObject implements SoundCarrierService {
    private static SoundCarrierServiceImpl instance;
    private final SoundCarrierRepository soundCarrierRepository;

    private SoundCarrierServiceImpl() throws RemoteException {
        soundCarrierRepository = HibernateSoundCarrierRepository.getInstance();
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album);

        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist) throws RemoteException {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist);

        return buildSoundCarrierDtos(soundCarriers);
    }

    private List<SoundCarrierDTO> buildSoundCarrierDtos(List<SoundCarrier> soundCarriers) {
        List<SoundCarrierDTO> soundCarrierDTOS = new LinkedList<>();

        for (SoundCarrier s : soundCarriers) {
            soundCarrierDTOS.add(SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getMedium().toString(), s.getPrice().toString(), s.getStock())
                    .withAlbumEntity(s.getAlbum().getName(), buildSongDtos(s.getAlbum().getSongs()))
                    .build());
        }

        return soundCarrierDTOS;
    }

    private SongDTO[] buildSongDtos(List<Song> songs) {
        SongDTO[] songDto = new SongDTO[songs.size()];

        for (int i = 0; i < songDto.length; i++) {
            Song s = songs.get(i);
            songDto[i] = SongDTO.builder().withSongEntity(s.getTitle(), s.getArtist(), s.getRelease().toString()).build();
        }
        return songDto;
    }

    public static SoundCarrierServiceImpl getInstance() throws RemoteException {
        if (instance == null) {
            instance = new SoundCarrierServiceImpl();
        }
        return instance;
    }
}
