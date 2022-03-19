package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;

import java.util.List;

public interface SoundCarrierRepository {

    List<SoundCarrier> allSoundCarriers();

    List<SoundCarrier> soundCarriersByAlbumName(String album);

    List<SoundCarrier> soundCarriersByArtistName(String artist);
}
