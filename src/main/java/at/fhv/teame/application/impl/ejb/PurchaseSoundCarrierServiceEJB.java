package at.fhv.teame.application.impl.ejb;


import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.connection.Session;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;

import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Stateless
public class PurchaseSoundCarrierServiceEJB implements PurchaseSoundCarrierServiceRemote {

    private final SoundCarrierRepository soundCarrierRepository;
    private final InvoiceRepository invoiceRepository;
    private final SessionRepository sessionRepository;

    // default constructor with hibernate
    public PurchaseSoundCarrierServiceEJB() {
        this(new HibernateInvoiceRepository(), new HibernateSoundCarrierRepository(), new ListSessionRepository());
    }

    // for mocking
    public PurchaseSoundCarrierServiceEJB(InvoiceRepository invoiceRepository, SoundCarrierRepository soundCarrierRepository, SessionRepository sessionRepository){
        this.soundCarrierRepository = soundCarrierRepository;
        this.invoiceRepository = invoiceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void confirmPurchase(ShoppingCartDTO shoppingCartDTO, String sessionId) throws PurchaseFailedException, InvalidSessionException{
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }
        if (shoppingCartDTO.getPurchasedItems().isEmpty()) {
            throw new PurchaseFailedException();
        }
        try {
            soundCarrierRepository.processPurchase(shoppingCartDTO.getPurchasedItems());
            invoiceRepository.add(createInvoice(shoppingCartDTO));
        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }

    private Invoice createInvoice(ShoppingCartDTO shoppingCartDTO) {
        Invoice invoice;
        if (shoppingCartDTO.getCustomerLastName().equalsIgnoreCase("guest")) {
            invoice = new Invoice(
                    LocalDate.now(),
                    PaymentMethod.valueOf(shoppingCartDTO.getPaymentMethod())
            );
        } else {
            invoice = new Invoice(
                    LocalDate.now(),
                    PaymentMethod.valueOf(shoppingCartDTO.getPaymentMethod()),
                    shoppingCartDTO.getCustomerFirstName(),
                    shoppingCartDTO.getCustomerLastName(),
                    shoppingCartDTO.getCustomerAddress()
            );
        }


        // start with totalPrice of 0
        BigDecimal totalPrice = new BigDecimal(0);
        List<InvoiceLine> purchasedItems = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : shoppingCartDTO.getPurchasedItems().entrySet()) {
            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(entry.getKey());

            int quantity = entry.getValue();
            BigDecimal price = soundCarrier.getPrice().multiply(new BigDecimal(quantity));

            InvoiceLine invoiceLine = new InvoiceLine(invoice, soundCarrier, quantity, price);

            purchasedItems.add(invoiceLine);
            totalPrice = totalPrice.add(price);
        }
        invoice.setPurchasedItems(purchasedItems);
        invoice.setTotalPrice(totalPrice.setScale(2,  RoundingMode.HALF_UP));
        return invoice;
    }
}