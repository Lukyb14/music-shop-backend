package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    @Override
    public List<SoundCarrier> allSoundCarriers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc", SoundCarrier.class);
        return query.getResultList();
    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String albumname) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT sc FROM SoundCarrier sc JOIN sc.album a WHERE a.name = :albumname", SoundCarrier.class);
        query.setParameter("albumname", albumname);
        return query.getResultList();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.artist = :artist", SoundCarrier.class);
        query.setParameter("artist", artist);
        return query.getResultList();
    }

}
