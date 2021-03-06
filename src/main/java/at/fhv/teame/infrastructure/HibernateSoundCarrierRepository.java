package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Stateless
public class HibernateSoundCarrierRepository implements SoundCarrierRepository {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    private final EntityManager entityManager = entityManagerFactory.createEntityManager();

    private static final int ROWS_PER_PAGE = 10;

    @Override
    public void processPurchase(Map<String, Integer> shoppingCartItems) throws OutOfStockException, InvalidAmountException {
        entityManager.getTransaction().begin();
        try {
            for (Map.Entry<String, Integer> entry : shoppingCartItems.entrySet()) {
                TypedQuery<SoundCarrier> query = entityManager.createQuery("FROM SoundCarrier sc WHERE sc.articleId = :articleId", SoundCarrier.class);
                query.setParameter("articleId", entry.getKey());
                SoundCarrier soundCarrier = query.getSingleResult();
                soundCarrier.retrieve(entry.getValue());
            }
            entityManager.getTransaction().commit();
        } catch (OutOfStockException | InvalidAmountException e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public void fillStock(String articleId, int amount) {
        entityManager.getTransaction().begin();
        TypedQuery<SoundCarrier> query = entityManager.createQuery("FROM SoundCarrier sc WHERE sc.articleId = :articleId", SoundCarrier.class);
        query.setParameter("articleId", articleId);
        SoundCarrier soundCarrier = query.getSingleResult();
        soundCarrier.fillStock(amount);
        entityManager.getTransaction().commit();
    }

    @Override
    public List<SoundCarrier> soundCarriersByAlbumName(String albumName, int pageNr) {
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a WHERE lower(a.name) LIKE lower(:albumName)", SoundCarrier.class);
        query.setParameter("albumName", albumName);
        query.setFirstResult((pageNr - 1) * ROWS_PER_PAGE);
        query.setMaxResults(ROWS_PER_PAGE);
        entityManager.clear();
        return query.getResultList();
    }

    @Override
    public Long totResultsByAlbumName(String albumName) {
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.name) LIKE lower(:albumName)");
        query.setParameter("albumName", albumName);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersByArtistName(String artist, int pageNr) {
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.artist) LIKE lower(:artist)", SoundCarrier.class);
        query.setParameter("artist", artist);
        query.setFirstResult((pageNr - 1) * ROWS_PER_PAGE);
        query.setMaxResults(ROWS_PER_PAGE);
        return query.getResultList();
    }

    @Override
    public Long totResultsByArtistName(String artist) {
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(a.artist) LIKE lower(:artist)");
        query.setParameter("artist", artist);
        return (Long) query.getSingleResult();
    }

    @Override
    public List<SoundCarrier> soundCarriersBySongName(String song, int pageNr) {
        TypedQuery<SoundCarrier> query = entityManager.createQuery("SELECT DISTINCT sc FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(s.title) LIKE lower(:song)", SoundCarrier.class);
        query.setParameter("song", song);
        query.setFirstResult((pageNr - 1) * ROWS_PER_PAGE);
        query.setMaxResults(ROWS_PER_PAGE);
        return query.getResultList();
    }

    @Override
    public Long totResultsBySongName(String song) {
        Query query = entityManager.createQuery("SELECT count(distinct sc) FROM SoundCarrier sc JOIN sc.album a JOIN a.songs s WHERE lower(s.title) LIKE lower(:song)");
        query.setParameter("song", song);
        return (Long) query.getSingleResult();
    }


    @Override
    public SoundCarrier soundCarrierByArticleId(String articleId) {
        TypedQuery<SoundCarrier> query = entityManager.createQuery("from SoundCarrier sc WHERE sc.articleId = :articleId", SoundCarrier.class);
        query.setParameter("articleId", articleId);
        return query.getSingleResult();
    }
}
