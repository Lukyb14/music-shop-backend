package at.fhv.teame.integration;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class IntegrationTest {
    private static SoundCarrierRepository soundCarrierRepository;
    private static InvoiceRepository invoiceRepository;

    @BeforeAll
    static void setup() {
        soundCarrierRepository = new HibernateSoundCarrierRepository();
        invoiceRepository = new HibernateInvoiceRepository();
    }


    // infrastructure - repository tests towards real db
    @Test
    void totResultsByAlbumName() {
        Long results = soundCarrierRepository.totResultsByAlbumName("Back in Black");

        assertTrue(results > 0);
    }

    @Test
    void soundCarriersBySongName() {
        List<SoundCarrier> soundCarriers = soundCarrierRepository.soundCarriersBySongName("Asphalt", 1);

        soundCarriers.forEach(sc -> assertTrue(sc.getAlbum().getSongs().stream().anyMatch(song -> song.getTitle().equals("Asphalt"))));
    }

    @Test
    void soundCarrierByArticleId() {
        SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId("295748");

        assertEquals("295748", soundCarrier.getArticleId());
    }

    @Test
    void fillStock() {
        SoundCarrier soundCarrierBeforeFill = soundCarrierRepository.soundCarrierByArticleId("295748");
        int expectedStock = soundCarrierBeforeFill.getStock() + 1;

        soundCarrierRepository.fillStock("295748", 1);

        SoundCarrier soundCarrierAfterFill = soundCarrierRepository.soundCarrierByArticleId("295748");
        int actualStock = soundCarrierAfterFill.getStock();

        assertEquals(expectedStock, actualStock);
    }

    @Test
    void invoiceById() {
        Invoice invoice = invoiceRepository.invoiceById(20028L);

        assertEquals(20028L, invoice.getInvoiceId());
    }
}
