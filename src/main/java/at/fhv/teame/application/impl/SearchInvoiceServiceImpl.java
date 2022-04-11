package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.rmi.SearchInvoiceService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SearchInvoiceServiceImpl extends UnicastRemoteObject implements SearchInvoiceService {
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

    public SearchInvoiceServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public InvoiceDTO invoiceById(String invoiceId) throws RemoteException {
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
