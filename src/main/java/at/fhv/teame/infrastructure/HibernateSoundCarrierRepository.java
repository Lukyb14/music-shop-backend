package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<SoundCarrier> getAllSoundCarriers() {
        return null;
    }

    @Override
    public List<SoundCarrier> getSoundCarrierByAlbumName() {
        return null;
    }
}
