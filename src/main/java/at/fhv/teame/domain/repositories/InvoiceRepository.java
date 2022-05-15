package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.invoice.Invoice;

import javax.ejb.Local;

@Local
public interface InvoiceRepository {
    void add(Invoice invoice);

    Invoice invoiceById(Long invoiceId);

    void updateInvoiceLine(String invoiceId, String articleId, int amountOfReturned);
}
