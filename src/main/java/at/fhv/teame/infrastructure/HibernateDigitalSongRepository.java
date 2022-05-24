package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalSongRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class HibernateDigitalSongRepository implements DigitalSongRepository {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    private static final int ROWS_PER_PAGE = 10;


    @Override
    public DigitalSong digitalSongByArticleId(Long articleId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong d WHERE d.id = :articleId", DigitalSong.class);
        query.setParameter("articleId", articleId);
        return query.getSingleResult();
    }

    @Override
    public List<DigitalSong> digitalSongByTitle(String title, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(title) LIKE lower(:title)", DigitalSong.class);
        query.setParameter("title", title);
        query.setFirstResult((pageNr - 1) * ROWS_PER_PAGE);
        query.setMaxResults(ROWS_PER_PAGE);
        return query.getResultList();
    }

    @Override
    public List<DigitalSong> digitalSongByArtist(String artist, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(artist) LIKE lower(:artist)", DigitalSong.class);
        query.setParameter("artist", artist);
        return query.getResultList();
    }

    @Override
    public List<DigitalSong> digitalSongByGenre(String genre, int pageNr) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalSong> query = entityManager.createQuery("FROM DigitalSong WHERE lower(genre) LIKE lower(:genre)", DigitalSong.class);
        query.setParameter("genre", genre);
        return query.getResultList();
    }
}
