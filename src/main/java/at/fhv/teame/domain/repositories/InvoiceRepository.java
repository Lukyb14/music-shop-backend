package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.invoice.Invoice;

import java.util.Optional;

public interface InvoiceRepository {
    void add(Invoice invoice);

    Optional<Invoice> invoiceById(String invoiceId);
}
