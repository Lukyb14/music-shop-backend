package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    private static final int RESULTS_PER_PAGE = 20;

    private static HibernateSoundCarrierRepository instance;
    private final EntityManagerFactory entityManagerFactory;

    public HibernateSoundCarrierRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");
    }

    @Override
    public List<SoundCarrier> allSoundCarriers() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc", SoundCarrier.class);
        return query.getResultList();
    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String albumName, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a WHERE a.name = :albumName", SoundCarrier.class);
        query.setFirstResult((pageNr - 1) * RESULTS_PER_PAGE);
        query.setMaxResults(RESULTS_PER_PAGE);
        query.setParameter("albumName", albumName);
        return query.getResultList();
    }

    @Override
    public Long numberOfSoundCarriersByAlbumName(String albumName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(s) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.name = :albumName");
        query.setParameter("albumName", albumName);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.artist = :artist", SoundCarrier.class);
        query.setFirstResult((pageNr - 1) * RESULTS_PER_PAGE);
        query.setMaxResults(RESULTS_PER_PAGE);
        query.setParameter("artist", artist);
        return query.getResultList();
    }

    @Override
    public Long numberOfSoundCarriersByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(s) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.artist = :artist");
        query.setParameter("artist", artist);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.title = :song", SoundCarrier.class);
        query.setFirstResult((pageNr - 1) * RESULTS_PER_PAGE);
        query.setMaxResults(RESULTS_PER_PAGE);
        query.setParameter("song", song);
        return query.getResultList();
    }

    @Override
    public Long numberOfSoundCarriersBySongName(String song) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(s) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.title = :song");
        query.setParameter("song", song);
        return (Long) query.getSingleResult();
    }

    public static HibernateSoundCarrierRepository getInstance() {
        if (instance == null) {
            instance = new HibernateSoundCarrierRepository();
        }
        return instance;
    }
}
