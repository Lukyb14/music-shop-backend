package at.fhv.teame.application.impl;

import at.fhv.teame.mocks.MockInvoiceRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.ejb.SearchInvoiceServiceRemote;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchInvoiceServiceImplTest {
    static SearchInvoiceServiceRemote searchInvoiceService;

    @BeforeAll
    static void beforeAll() {
        searchInvoiceService = new SearchInvoiceServiceImpl(new MockInvoiceRepository());
    }

    @Test
    void given_invoiceinrepository_when_invoiceById_then_expectInvoiceequals() {
        //given
        InvoiceLineDTO[] invoiceLineDTOExpected = new InvoiceLineDTO[1];

        invoiceLineDTOExpected[0] = InvoiceLineDTO.builder()
                .withInvoiceLineEntity("10000",
                        "Bob",
                        "Sun",
                        "CD", 5,
                        1,
                        "4")
                .build();

        InvoiceDTO invoiceDTOExpected = InvoiceDTO.builder()
                .withInvoiceEntity("20000",
                        "2022.04.10",
                        "Cash",
                        "3",
                        invoiceLineDTOExpected)
                .build();

        //when
        InvoiceDTO invoiceDTOActual = searchInvoiceService.invoiceById("20000");

        //then
        assertEquals(invoiceDTOActual.getInvoiceId(), invoiceDTOExpected.getInvoiceId());
    }
}


