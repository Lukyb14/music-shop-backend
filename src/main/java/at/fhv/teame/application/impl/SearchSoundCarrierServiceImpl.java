package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDetailsDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;

import javax.ejb.Stateless;
import java.util.LinkedList;
import java.util.List;

@Stateless
public class SearchSoundCarrierServiceImpl implements SearchSoundCarrierServiceRemote {
    private final SoundCarrierRepository soundCarrierRepository;

    public SearchSoundCarrierServiceImpl() {
        this(new HibernateSoundCarrierRepository());
    }

    public SearchSoundCarrierServiceImpl(SoundCarrierRepository soundCarrierRepository) {
        this.soundCarrierRepository = soundCarrierRepository;
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersByAlbumName(String album, int pageNr) {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByAlbumName(album, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }


    @Override
    public List<SoundCarrierDTO> soundCarriersByArtistName(String artist, int pageNr) {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersByArtistName(artist, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public List<SoundCarrierDTO> soundCarriersBySongName(String song, int pageNr) {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName(song, pageNr);
        return buildSoundCarrierDtos(soundCarriers);
    }

    @Override
    public int totResultsByAlbumName(String album) {
        return soundCarrierRepository.totResultsByAlbumName(album).intValue();
    }

    @Override
    public int totResultsByArtistName(String artist) {
        return soundCarrierRepository.totResultsByArtistName(artist).intValue();
    }

    @Override
    public int totResultsBySongName(String song) {
        return soundCarrierRepository.totResultsBySongName(song).intValue();
    }

    @Override
    public SoundCarrierDetailsDTO soundCarrierDetailsByArticleId(String articleId) {
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
