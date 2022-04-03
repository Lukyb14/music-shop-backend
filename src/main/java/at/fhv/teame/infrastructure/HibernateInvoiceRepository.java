package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.repositories.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

public class HibernateInvoiceRepository implements InvoiceRepository {
    private final EntityManagerFactory entityManagerFactory;

    public HibernateInvoiceRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");
    }

    @Override
    public void add(Invoice invoice) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(invoice);
        entityManager.getTransaction().commit();
    }

    @Override
    public Optional<Invoice> invoiceById(String invoiceId) {
        return Optional.empty();
    }
}
