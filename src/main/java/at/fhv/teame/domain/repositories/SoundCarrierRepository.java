package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;

import java.util.List;

public interface SoundCarrierRepository {

    public List<SoundCarrier> allSoundCarriers();

    public List<SoundCarrier> soundCarriersByAlbumName(String album);

    public List<SoundCarrier> soundCarriersByArtistName(String artist);
}
