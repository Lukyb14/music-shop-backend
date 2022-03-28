package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {

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
        query.setParameter("albumName", albumName);
        return query.getResultList();
    }

    @Override
    public Long nrOfRowsByAlbumName(String albumName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(s) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.name = :albumName");
        query.setParameter("albumName", albumName);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println(artist);
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.artist=:artist", SoundCarrier.class);
        query.setParameter("artist", artist);
        //query.setFirstResult((pageNr - 1) * ROWS_PER_PAGE);

        System.out.println("pageNr: "+pageNr);
        return query.getResultList();
    }

    @Override
    public Long nrOfRowsByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(s) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE a.artist = :artist");
        query.setParameter("artist", artist);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE s.title = :song", SoundCarrier.class);
        query.setParameter("song", song);
        return query.getResultList();
    }

    @Override
    public Long nrOfRowsBySongName(String song) {
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
