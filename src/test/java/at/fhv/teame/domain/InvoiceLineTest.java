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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceLineTest {

    @Test
    void testInvoiceLineConstructor() {
        BigDecimal totPrice = new BigDecimal("31.31");
        Invoice invoice = new Invoice(LocalDate.of(2022,4,4), PaymentMethod.CASH);
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("1011", album, Medium.CD, new BigDecimal("31.31"), 10);
        int quantity = 3;

        InvoiceLine invoiceLine = new InvoiceLine(invoice, soundCarrier, quantity, totPrice);

        assertEquals(totPrice, invoiceLine.getPrice());
        assertEquals(invoice, invoiceLine.getInvoice());
        assertEquals(soundCarrier, invoiceLine.getSoundCarrier());
        assertEquals(quantity, invoiceLine.getQuantity());
    }


    @Test
    void testUpdateAmountOfReturnedItems() {
        int amountOfReturnedItems = 2;
        BigDecimal totPrice = new BigDecimal("31.31");
        Invoice invoice = new Invoice(LocalDate.of(2022,4,4), PaymentMethod.CASH);
        List<Song> songs = new ArrayList<>();
        Song song1 = new Song("Money For All", LocalDate.of(1985, 1, 1), "03:53");
        songs.add(song1);

        Album album = new Album("Testname", "TestLabel",
                LocalDate.of(1985,1,1),
                songs, "Rock", "TestArtist");

        SoundCarrier soundCarrier = new SoundCarrier("1011", album, Medium.CD, new BigDecimal("31.31"), 10);
        int quantity = 3;

        InvoiceLine invoiceLine = new InvoiceLine(invoice, soundCarrier, quantity, totPrice);
        invoiceLine.updateAmountOfReturnedItems(amountOfReturnedItems);

        assertEquals(amountOfReturnedItems, invoiceLine.getAmountOfReturnedItems());
    }
}
