package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.model.onlineshop.DigitalSong;
import at.fhv.teame.domain.repositories.DigitalInvoiceRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class HibernateDigitalInvoiceRepository implements DigitalInvoiceRepository {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    @Override
    public void store(DigitalInvoice digitalInvoice) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(digitalInvoice);
        for (DigitalInvoiceLine invoiceLine : digitalInvoice.getPurchasedItems()){
            entityManager.persist(invoiceLine);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public List<DigitalInvoice> digitalInvoicesByEmail(String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<DigitalInvoice> query = entityManager.createQuery("FROM DigitalInvoice WHERE lower(email) LIKE lower(:email)", DigitalInvoice.class);
        query.setParameter("email", email);
        return query.getResultList();
    }
}
