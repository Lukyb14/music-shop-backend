package at.fhv.teame.domain.repositories;

import at.fhv.teame.domain.model.onlineshop.DigitalInvoice;

import javax.ejb.Local;
import java.util.List;

@Local
public interface DigitalInvoiceRepository {
    void store(DigitalInvoice digitalInvoice);

    List<DigitalInvoice> digitalInvoicesByEmail(String email);
}
