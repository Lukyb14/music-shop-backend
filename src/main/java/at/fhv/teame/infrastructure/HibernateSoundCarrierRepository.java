package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import javax.persistence.*;
import java.util.List;

public class HibernateSoundCarrierRepository implements SoundCarrierRepository {
    private final EntityManagerFactory entityManagerFactory;
    private static final int ROWS_PER_PAGE = 10;

    public HibernateSoundCarrierRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");
    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String albumName, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a WHERE lower(a.name) = lower(:albumName)", SoundCarrier.class);
        query.setParameter("albumName", albumName);
        int firstResult = (pageNr - 1) * ROWS_PER_PAGE;
        int maxResults = calcMaxResults(totResultsByAlbumName(albumName).intValue(), pageNr);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public Long totResultsByAlbumName(String albumName) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.name) = lower(:albumName)");
        query.setParameter("albumName", albumName);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println(artist);
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.artist) = lower(:artist)", SoundCarrier.class);
        query.setParameter("artist", artist);
        int firstResult = (pageNr - 1) * ROWS_PER_PAGE;
        int maxResults = calcMaxResults(totResultsByArtistName(artist).intValue(), pageNr);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public Long totResultsByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.artist) = lower(:artist)");
        query.setParameter("artist", artist);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(s.title) = lower(:song)", SoundCarrier.class);
        query.setParameter("song", song);
        int firstResult = (pageNr - 1) * ROWS_PER_PAGE;
        int maxResults = calcMaxResults(totResultsBySongName(song).intValue(), pageNr);
        query.setFirstResult(firstResult);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    @Override
    public Long totResultsBySongName(String song) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(s.title) = lower(:song)");
        query.setParameter("song", song);
        return (Long) query.getSingleResult();
    }


    @Override
    public SoundCarrier soundCarrierByArticleId(String articleId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc WHERE sc.articleId = :articleId", SoundCarrier.class);
        query.setParameter(articleId, articleId);
        return query.getSingleResult();
    }

    private int calcMaxResults(int totResultsInMatch, int pageNr) {
        int maxResults;
        if ((totResultsInMatch / (ROWS_PER_PAGE * pageNr)) > 0) {
            maxResults = ROWS_PER_PAGE;
        } else {
            maxResults = totResultsInMatch % ROWS_PER_PAGE;
        }
        return maxResults;
    }
}
