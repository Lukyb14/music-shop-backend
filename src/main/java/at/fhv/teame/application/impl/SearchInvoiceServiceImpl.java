package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.ejb.SearchInvoiceServiceRemote;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;

@Stateless
public class SearchInvoiceServiceImpl implements SearchInvoiceServiceRemote {
    @EJB
    private InvoiceRepository invoiceRepository;

    public SearchInvoiceServiceImpl() {}

    //for mocking
    public SearchInvoiceServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    @Override
    public InvoiceDTO invoiceById(String invoiceId) {
        try{
            Invoice invoice = invoiceRepository.invoiceById(Long.valueOf(invoiceId));
            return buildInvoiceDTO(invoice);
        } catch (NoResultException noResultException){
            return null;
        }
    }

    private InvoiceDTO buildInvoiceDTO(Invoice invoice) {
        return InvoiceDTO.builder().withInvoiceEntity(invoice.getInvoiceId().toString(),
                        invoice.getDateOfPurchase().toString(),
                        invoice.getPaymentMethod().toString(),
                        invoice.getToRefund().toString(),
                        buildInvoiceLineDTO(invoice.getPurchasedItems()))
                .withCustomerEntity(invoice.getCustomerFirstName().orElse(null),
                        invoice.getCustomerLastName().orElse(null),
                        invoice.getCustomerAddress().orElse(null))
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
