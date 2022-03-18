package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public List<SoundCarrier> allSoundCarriers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc", SoundCarrier.class);
        return query.getResultList();
    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String album) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc where sc.album = :album", SoundCarrier.class);
        query.setParameter("album", album);
        return query.getResultList();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc where sc.album.artist = :artist", SoundCarrier.class);
        query.setParameter("artist", artist);
        return query.getResultList();
    }

}
