package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;

import javax.ejb.Local;

@Local
public interface DigitalInvoiceRepository {
    void add(DigitalInvoice digitalInvoice);
}
