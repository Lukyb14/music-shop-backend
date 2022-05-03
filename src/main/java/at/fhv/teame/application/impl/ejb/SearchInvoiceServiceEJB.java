package at.fhv.teame.application.impl.ejb;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.connection.Session;
import at.fhv.teame.sharedlib.dto.InvoiceDTO;
import at.fhv.teame.sharedlib.dto.InvoiceLineDTO;
import at.fhv.teame.sharedlib.ejb.SearchInvoiceServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

@Stateless
public class SearchInvoiceServiceEJB implements SearchInvoiceServiceRemote {
    private final InvoiceRepository invoiceRepository;
    private final SessionRepository sessionRepository;

    // default constructor with hibernate
    public SearchInvoiceServiceEJB() {
        this(new HibernateInvoiceRepository(), new ListSessionRepository());
    }

    //for mocking
    public SearchInvoiceServiceEJB(InvoiceRepository invoiceRepository, SessionRepository sessionRepository) {
        this.invoiceRepository = invoiceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public InvoiceDTO invoiceById(String invoiceId, String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }
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