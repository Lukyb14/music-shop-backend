package at.fhv.teame.domain;

import at.fhv.teame.application.impl.PurchaseSoundCarrierServiceImpl;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.PaymentMethod;
import at.fhv.teame.domain.model.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    private PurchaseSoundCarrierServiceImpl purchaseSoundCarrierService;
    private HibernateInvoiceRepository invoiceRepository;

    @BeforeEach
    void setup() throws IOException {
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl();
        invoiceRepository = new HibernateInvoiceRepository();
    }

    @Test
    void invoiceIntTest() {
        Map<String, Integer> shoppingCart = new HashMap<>();
        shoppingCart.put("1121", 1);
        String paymentMethod = "cash";
        BigDecimal totalPrice = new BigDecimal("201");

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCart, paymentMethod, totalPrice);
        invoiceRepository.add(invoice);

        System.out.println(invoice.getInvoiceId());

    }

  /*  @Test
    void invoiceNumberTest() {
        SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
        InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("1001");

        Invoice invoice = new Invoice(LocalDate.now(), PaymentMethod.CASH, new BigDecimal("100"));

        invoice.addInvoiceItem(new InvoiceLine(invoice, soundCarrier, 1, soundCarrier.getPrice()));

        assertEquals(10000, invoice.getInvoiceId());
    }*/
}