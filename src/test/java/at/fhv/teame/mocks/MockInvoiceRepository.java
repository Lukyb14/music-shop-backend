package at.fhv.teame.mocks;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class MockInvoiceRepository implements InvoiceRepository{
    @Override
    public void add(Invoice invoice) {

    }

    @Override
    public Invoice invoiceById(Long invoiceId) {
        Invoice invoice = new Invoice(LocalDate.of(2022, 04, 10), PaymentMethod.CASH, "Max", "Mustermann", "Testgasse");
        Field invoiceIdField = null;
        try {
             invoiceIdField = Invoice.class.getDeclaredField("invoiceId");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        invoiceIdField.setAccessible(true);
        try {
            invoiceIdField.set(invoice, 20000L);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        List<InvoiceLine> invoicelines = List.of(
                new InvoiceLine(invoice, createSoundCarrierDummy(),5, BigDecimal.valueOf(4))
        );

        invoice.setPurchasedItems(invoicelines);

        return invoice;
    }

    @Override
    public void updateInvoiceLine(String invoiceId, String articleId, int amountOfReturned) {

    }

    public SoundCarrier createSoundCarrierDummy() {
        return new SoundCarrier(
                String.valueOf(100),
                new Album("Black and White", "Black bars", LocalDate.of(2022,04,11), createSongListDummy(), "Pop", "Bob"),
                Medium.VINYL,
                BigDecimal.valueOf(20),
                3
        );
    }


    public List<Song> createSongListDummy() {
        return Arrays.asList(
                new Song("Hello World", LocalDate.of(2022, 04, 10), "2:42"),
                new Song("Apple M1", LocalDate.of(2022, 02, 10), "3:42"),
                new Song("Understruck", LocalDate.of(2022, 03, 10), "4:13")
        );
    }
}
