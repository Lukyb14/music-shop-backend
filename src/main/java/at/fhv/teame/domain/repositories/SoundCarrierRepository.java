package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.SoundCarrier;

import java.util.List;

public interface SoundCarrierRepository {

    public List<SoundCarrier> getAllSoundCarriers();

    public List<SoundCarrier> getSoundCarrierByAlbumName();
}
