package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

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
        for (InvoiceLine invoiceLine : invoice.getPurchasedItems()){
            entityManager.persist(invoiceLine);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public Invoice invoiceById(Long invoiceId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        TypedQuery<Invoice> query = entityManager.createQuery("from Invoice invoice WHERE invoice.invoiceId = :invoiceId", Invoice.class);
        query.setParameter("invoiceId", invoiceId);
        return query.getSingleResult();
    }


}
