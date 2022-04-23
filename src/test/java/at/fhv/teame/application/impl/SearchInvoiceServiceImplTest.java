package at.fhv.teame.application.impl;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.mocks.MockInvoiceRepository;
import at.fhv.teame.mocks.MockSessionRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchInvoiceServiceImplTest {
    static SearchInvoiceServiceImpl searchInvoiceService;

    @BeforeAll
    static void beforeAll() throws RemoteException{
        searchInvoiceService = new SearchInvoiceServiceImpl(new MockInvoiceRepository(), new MockSessionRepository());
    }

    @Test
    void given_2invoiceinrepository_when_invoiceById_then_expectInvoiceequals() throws RemoteException, InvalidSessionException {
        //given
        InvoiceLineDTO[] invoiceLineDTOExpected = new InvoiceLineDTO[1];
        InvoiceLineDTO.Builder builder = InvoiceLineDTO.builder();
        builder.withInvoiceLineEntity("10000", "Bob", "Sun", "CD", 5, 1, "4");
        invoiceLineDTOExpected[0] = builder.build();

        InvoiceDTO invoiceDTOExpected = InvoiceDTO.builder().withInvoiceEntity("20000", "2022.04.10", "Cash", "3", invoiceLineDTOExpected).build();

        //when
        InvoiceDTO invoiceDTOActual = searchInvoiceService.invoiceById("20000", UUID.randomUUID().toString());


        //then
        assertEquals(invoiceDTOActual.getInvoiceId(), invoiceDTOExpected.getInvoiceId());
    }
}


