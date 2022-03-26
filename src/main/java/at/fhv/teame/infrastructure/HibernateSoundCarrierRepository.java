package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

    private static final int RESULTS_PER_PAGE = 20;

    private static HibernateSoundCarrierRepository instance;
    private final EntityManagerFactory entityManagerFactory;

    private HibernateSoundCarrierRepository() {
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
    public int numberOfSoundCarriersByAlbumName(String albumName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a WHERE a.name = :albumName", SoundCarrier.class);
        query.setParameter("albumName", albumName);
        return query.getResultList().size();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.artist = :artist", SoundCarrier.class);
        query.setFirstResult((pageNr - 1) * RESULTS_PER_PAGE);
        query.setMaxResults(RESULTS_PER_PAGE);
        query.setParameter("artist", artist);
        return query.getResultList();
    }

    @Override
    public int numberOfSoundCarriersByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.artist = :artist", SoundCarrier.class);
        query.setParameter("artist", artist);
        return query.getResultList().size();
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
    public int numberOfSoundCarriersBySongName(String song) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.title = :song", SoundCarrier.class);
        query.setParameter("song", song);
        return query.getResultList().size();
    }

    public static HibernateSoundCarrierRepository getInstance() {
        if (instance == null) {
            instance = new HibernateSoundCarrierRepository();
        }
        return instance;
    }
}
