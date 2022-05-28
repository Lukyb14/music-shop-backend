package at.fhv.teame.domain;


import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvoiceTest {

    @Test
    void getInvoiceById() {
        Long invoiceIdExpected = null;
        Invoice invoice = new Invoice(LocalDateTime.of(2022,4,16, 0, 0, 0), PaymentMethod.CASH, "Hiranur", "Mueller", "Wildschwansteig 24, 13503 Berlin");
        assertEquals(invoiceIdExpected, invoice.getInvoiceId());
    }

    @Test
    void testInvoiceConstructorWithoutCustomer() {
        //given
        Invoice invoice = new Invoice(LocalDateTime.of(2022,4,4, 0, 0, 0), PaymentMethod.CASH);

        //when...then

        assertEquals(LocalDateTime.of(2022,4,4, 0, 0, 0), invoice.getDateOfPurchase());
        assertEquals(PaymentMethod.CASH, invoice.getPaymentMethod());
    }

    @Test
    void testInvoiceConstructorWithCustomer() {
        Invoice invoice = new Invoice(LocalDateTime.of(2022,4,4, 0, 0, 0), PaymentMethod.CASH,
                "Umut", "Mueller",
                "Kanal31, 6900 Bregenz");
        //when...then
        assertEquals(LocalDateTime.of(2022,4,4, 0, 0, 0), invoice.getDateOfPurchase());
        assertEquals(PaymentMethod.CASH, invoice.getPaymentMethod());
        assertEquals("Umut", invoice.getCustomerFirstName().orElse(""));
        assertEquals("Mueller", invoice.getCustomerLastName().orElse(""));
        assertEquals("Kanal31, 6900 Bregenz", invoice.getCustomerAddress().orElse(""));

    }

    @Test
    void testPurchasedItems() {
        BigDecimal totPrice = new BigDecimal("31.31");
        Invoice invoice = new Invoice(LocalDateTime.of(2022,4,4, 0, 0, 0), PaymentMethod.CASH);
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("1011", album, Medium.CD, new BigDecimal("31.31"), 10);
        int quantity = 3;
        List<InvoiceLine> purchasedItems = new ArrayList<>();
        InvoiceLine invoiceLine1 = new InvoiceLine(invoice, soundCarrier, quantity, totPrice);
        purchasedItems.add(invoiceLine1);
        invoice.setPurchasedItems(purchasedItems);

        assertEquals(purchasedItems, invoice.getPurchasedItems());
    }

    @Test
    void testTotalPrice() {
        BigDecimal totPrice = new BigDecimal("31.31");
        Invoice invoice = new Invoice(LocalDateTime.of(2022,4,4, 0, 0, 0), PaymentMethod.CASH);

        invoice.setTotalPrice(totPrice);


        assertEquals(totPrice, invoice.getTotalPrice());

    }
}