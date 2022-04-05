package at.fhv.teame.domain;

import at.fhv.teame.application.impl.PurchaseSoundCarrierServiceImpl;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCart, paymentMethod);
        invoiceRepository.add(invoice);

        System.out.println(invoice.getInvoiceId());

    }

    @Test
    void getInvoiceByIdTest() {
        // given
        Long invoiceIdExpected = Long.valueOf(20001);

        // when
        Invoice invoice = invoiceRepository.invoiceById(invoiceIdExpected);
        Long invoiceIdActual = invoice.getInvoiceId();
        // then
        assertEquals(invoiceIdExpected, invoiceIdActual);
    }

    @Test
    void invoiceNumberTest() {
        SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();

        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("1001");

        Invoice invoice = new Invoice(LocalDate.now(), PaymentMethod.CASH);
        invoice.setTotalPrice(new BigDecimal("100"));

        List<InvoiceLine> invoiceLineList = List.of(new InvoiceLine(invoice, soundCarrier, 1, soundCarrier.getPrice()));
        invoice.setPurchasedItems(invoiceLineList);

        assertEquals(10000, invoice.getInvoiceId());
    }

    @Test
    void invoicePriceCalculationTest() {
        Map<String, Integer> shoppingCartItemsExpected = Map.of(
                "1121", 2,
                "1122", 1,
                "1123", 2
        );

        BigDecimal totalPriceExpected = new BigDecimal(0);
        BigDecimal price1 = new BigDecimal(46.28).multiply(new BigDecimal(2)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal price2 = new BigDecimal(14.30).multiply(new BigDecimal(1)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal price3 = new BigDecimal(11.26).multiply(new BigDecimal(2)).setScale(2, RoundingMode.HALF_UP);

        totalPriceExpected = totalPriceExpected.add(price1).add(price2).add(price3).setScale(2, RoundingMode.HALF_UP);
        String paymentMethodExpected = "cash";

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCartItemsExpected, paymentMethodExpected);

        assertEquals(totalPriceExpected, invoice.getTotalPrice());
        assertEquals(shoppingCartItemsExpected.size(), invoice.getPurchasedItems().size());
        assertEquals(paymentMethodExpected.toUpperCase(Locale.ROOT), invoice.getPaymentMethod().toString());
    }
}