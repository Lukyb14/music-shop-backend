package at.fhv.teame.application.impl;

import at.fhv.teame.application.SearchSoundCarrierService;
import at.fhv.teame.domain.Song;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;

import java.util.LinkedList;
import java.util.List;

public class SearchSoundCarrierImplementation implements SearchSoundCarrierService {


    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();

    @Override
    public List<SoundCarrierDTO> getAllSoundCarriers() {

        List<SoundCarrier> soundCarriers = soundCarrierRepository.allSoundCarriers();

        List<SoundCarrierDTO> soundCarrierDTOS = new LinkedList<>();

        for (SoundCarrier s : soundCarriers) {
            soundCarrierDTOS.add(SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getMedium().toString(), s.getPrice().toString(), s.getStock())
                    .withAlbumEntity(s.getAlbum().getName(), buildSongDto(s.getAlbum().getSongs()))
                    .build());
        }

        return soundCarrierDTOS;
    }

    private SongDTO[] buildSongDto(List<Song> songs) {
        SongDTO[] songDto = new SongDTO[songs.size()];

        for (int i = 0; i < songDto.length; i++) {
            Song s = songs.get(i);
            songDto[i] = SongDTO.builder().withSongEntity(s.getTitle(), s.getArtist(), s.getRelease().toString()).build();
        }
        return songDto;
    }
}