package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.invoice.Invoice;

public interface InvoiceRepository {
    void add(Invoice invoice);

    Invoice invoiceById(Long invoiceId);
}
