package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;

import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.List;

@Stateless
public class HibernateDigitalSongRepository implements DigitalSongRepository {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    //private static final int ROWS_PER_PAGE = 10;

    @Override
    public DigitalSong digitalSongByArticleId(Long articleId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong d WHERE d.id = :articleId", DigitalSong.class);
        query.setParameter("articleId", articleId);
        return query.getSingleResult();
    }

    @Override
    public List<DigitalSong> digitalSongByTitle(String title, int pageNr, int pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(title) LIKE lower(:title)", DigitalSong.class);
        query.setParameter("title", title);
        query.setFirstResult((pageNr - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<DigitalSong> digitalSongByArtist(String artist, int pageNr, int pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(artist) LIKE lower(:artist)", DigitalSong.class);
        query.setParameter("artist", artist);
        query.setFirstResult((pageNr - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public List<DigitalSong> digitalSongByGenre(String genre, int pageNr, int pageSize) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(genre) LIKE lower(:genre)", DigitalSong.class);
        query.setParameter("genre", genre);
        query.setFirstResult((pageNr - 1) * pageSize);
        query.setMaxResults(pageSize);
        return query.getResultList();
    }

    @Override
    public Long totResultsByTitle(String title) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(ds) FROM DigitalSong ds WHERE lower(title) LIKE lower(:title)");
        query.setParameter("title", title);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long totResultsByArtistName(String artist) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(ds) FROM DigitalSong ds WHERE lower(artist) LIKE lower(:artist)");
        query.setParameter("artist", artist);
        return (Long) query.getSingleResult();
    }

    @Override
    public Long totResultsByGenre(String genre) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query query = entityManager.createQuery("SELECT count(ds) FROM DigitalSong ds WHERE lower(genre) LIKE lower(:genre)");
        query.setParameter("genre", genre);
        return (Long) query.getSingleResult();
    }
}
