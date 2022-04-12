package at.fhv.teame.application.impl;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.rmi.Session;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.rmi.SearchInvoiceService;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class SearchInvoiceServiceImpl extends UnicastRemoteObject implements SearchInvoiceService {
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();
    private final SessionRepository sessionRepository = new ListSessionRepository();

    public SearchInvoiceServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public InvoiceDTO invoiceById(String invoiceId, String sessionId) throws RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

       Invoice invoice = invoiceRepository.invoiceById(Long.valueOf(invoiceId));
       return buildInvoiceDTO(invoice);

    }

    private InvoiceDTO buildInvoiceDTO(Invoice invoice) {

        return InvoiceDTO.builder().withInvoiceEntity(invoice.getInvoiceId().toString(),
                        invoice.getDateOfPurchase().toString(),
                        invoice.getPaymentMethod().toString(),
                        invoice.getToRefund().toString(),
                        buildInvoiceLineDTO(invoice.getPurchasedItems()))
                .withCustomerEntity(invoice.getCustomerFirstName(),
                        invoice.getCustomerLastName(),
                        invoice.getCustomerAddress())
                .build();
    }

    private InvoiceLineDTO[] buildInvoiceLineDTO (List<InvoiceLine> invoiceLines) {

        InvoiceLineDTO[] invoiceLineDto = new InvoiceLineDTO[invoiceLines.size()];

        for(int i = 0; i <invoiceLineDto.length; i++){
            InvoiceLine invoiceLine = invoiceLines.get(i);
            invoiceLineDto[i] = InvoiceLineDTO.builder().withInvoiceLineEntity(invoiceLine.getSoundCarrier().getArticleId(),
                    invoiceLine.getSoundCarrier().getAlbumArtist(), 
                    invoiceLine.getSoundCarrier().getAlbumName(),
                    invoiceLine.getSoundCarrier().getMedium(), invoiceLine.getQuantity(),
                    invoiceLine.getAmountOfReturnedItems(), invoiceLine.getPrice().toString()).build();
        }
        return invoiceLineDto;
    }
}
