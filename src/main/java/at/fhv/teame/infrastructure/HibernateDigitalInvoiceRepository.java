package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;
import at.fhv.teame.domain.model.onlineshop.DigitalInvoiceLine;
import at.fhv.teame.domain.repositories.DigitalInvoiceRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Stateless
public class HibernateDigitalInvoiceRepository implements DigitalInvoiceRepository {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    @Override
    public void add(DigitalInvoice digitalInvoice) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(digitalInvoice);
        for (DigitalInvoiceLine invoiceLine : digitalInvoice.getPurchasedItems()){
            entityManager.persist(invoiceLine);
        }
        entityManager.getTransaction().commit();
    }
}
