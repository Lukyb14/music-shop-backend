package at.fhv.teame.infrastructure;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

@Stateless
public class HibernateInvoiceRepository implements InvoiceRepository {
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");
    private EntityManager entityManager = entityManagerFactory.createEntityManager();


    @Override
    public void add(Invoice invoice) {
        entityManager.getTransaction().begin();
        entityManager.persist(invoice);
        for (InvoiceLine invoiceLine : invoice.getPurchasedItems()) {
            entityManager.persist(invoiceLine);
        }
        entityManager.getTransaction().commit();
    }

    @Override
    public Invoice invoiceById(Long invoiceId) {
        TypedQuery<Invoice> query = entityManager.createQuery("FROM Invoice invoice WHERE invoice.invoiceId = :invoiceId", Invoice.class);
        query.setParameter("invoiceId", invoiceId);
        return query.getSingleResult();
    }

    @Override
    public void updateInvoiceLine(String invoiceId, String articleId, int returnAmount) {
        entityManager.getTransaction().begin();
        TypedQuery<InvoiceLine> query = entityManager.createQuery("SELECT DISTINCT il FROM InvoiceLine il JOIN SoundCarrier as s on s.id = il.soundCarrier.id WHERE s.articleId = :articleId AND il.invoice.id = :invoiceId", InvoiceLine.class);
        query.setParameter("invoiceId", Long.valueOf(invoiceId));
        query.setParameter("articleId", articleId);
        InvoiceLine invoiceLine = query.getSingleResult();
        invoiceLine.updateAmountOfReturnedItems(returnAmount);
        entityManager.getTransaction().commit();
    }
}
