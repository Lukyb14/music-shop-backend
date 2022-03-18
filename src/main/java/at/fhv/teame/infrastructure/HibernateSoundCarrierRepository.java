package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<SoundCarrier> getAllSoundCarriers() {
        return null;
    }

    @Override
    public List<SoundCarrier> getSoundCarrierByAlbumName() {
        return null;
    }
}
