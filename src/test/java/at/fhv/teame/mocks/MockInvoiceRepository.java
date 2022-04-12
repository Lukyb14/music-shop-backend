package at.fhv.teame.mocks;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.repositories.InvoiceRepository;

public class MockInvoiceRepository implements InvoiceRepository{
    @Override
    public void add(Invoice invoice) {

    }

    @Override
    public Invoice invoiceById(Long invoiceId) {
        return null;
    }

    @Override
    public void updateInvoiceLine(String invoiceId, String articleId, int amountOfReturned) {

    }
}
