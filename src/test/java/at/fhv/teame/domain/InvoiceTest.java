package at.fhv.teame.domain;

import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {
    @Test
    void invoiceNumberTest() {
        SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
        InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("1001");

        Invoice invoice = new Invoice(LocalDate.now(), PaymentMethod.CASH, new BigDecimal("100"));

        invoice.addInvoiceItem(new InvoiceLine(invoice, soundCarrier, 1, soundCarrier.getPrice()));

        assertEquals("10000", invoice.getInvoiceId());
    }
}