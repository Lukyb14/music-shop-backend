package at.fhv.teame.domain;

import at.fhv.teame.application.impl.PurchaseSoundCarrierServiceImpl;
import at.fhv.teame.application.impl.SearchInvoiceServiceImpl;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InvoiceTest {

    private SearchInvoiceServiceImpl searchInvoiceService;
    private PurchaseSoundCarrierServiceImpl purchaseSoundCarrierService;
    private HibernateInvoiceRepository invoiceRepository;

    @BeforeEach
    void setup() throws IOException {
        searchInvoiceService = new SearchInvoiceServiceImpl();
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl();
        invoiceRepository = new HibernateInvoiceRepository();
    }

    @Test
    void invoiceWithoutCustomerTest() {
        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put("1161", 1);
        String paymentMethod = "cash";

        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        purchasedItems,
                        paymentMethod,
                        "guest",
                        "",
                        ""
                ).build();

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCartDTO);
        invoiceRepository.add(invoice);

        System.out.println(invoice.getInvoiceId());
    }

    @Test
    void invoiceWithCustomerTest() {
        Map<String, Integer> purchasedItems = new HashMap<>();
        purchasedItems.put("10001", 1);
        String paymentMethod = "cash";

        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        purchasedItems,
                        paymentMethod,
                        "Rainer",
                        "Zufall",
                        "Keinegültigeaddresse 1, 1000 Niemandshausen"
                ).build();

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCartDTO);
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

        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        shoppingCartItemsExpected,
                        paymentMethodExpected,
                        "guest",
                        "",
                        ""
                ).build();

        Invoice invoice = purchaseSoundCarrierService.createInvoice(shoppingCartDTO);

        assertEquals(totalPriceExpected, invoice.getTotalPrice());
        assertEquals(shoppingCartItemsExpected.size(), invoice.getPurchasedItems().size());
        assertEquals(paymentMethodExpected.toUpperCase(Locale.ROOT), invoice.getPaymentMethod().toString());
    }

    @Test
    void testSearchInvoiceServiceImpl() throws RemoteException {
        InvoiceDTO invoiceDTO = searchInvoiceService.invoiceById("20000");
        assertEquals("20000", invoiceDTO.getInvoiceId());
        assertEquals("14.30", invoiceDTO.getToRefund());
        assertEquals("Keinegültigeaddresse 1, 1000 Niemandshausen", invoiceDTO.getCustomerAddress());
        assertEquals("Rainer", invoiceDTO.getCustomerFirstName());
        assertEquals("Zufall", invoiceDTO.getCustomerLastName());
        assertEquals("2022-04-07", invoiceDTO.getPurchaseDate());
        assertEquals("CASH", invoiceDTO.getPaymentMethod());

    }
}