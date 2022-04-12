package at.fhv.teame.application.impl;


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
import at.fhv.teame.rmi.Session;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.*;

public class PurchaseSoundCarrierServiceImpl extends UnicastRemoteObject implements PurchaseSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository;
    private final InvoiceRepository invoiceRepository;
    private final SessionRepository sessionRepository;

    // default constructor with hibernate
    public PurchaseSoundCarrierServiceImpl() throws RemoteException {
        this(new HibernateInvoiceRepository(), new HibernateSoundCarrierRepository());
    }

    // for mocking
    public PurchaseSoundCarrierServiceImpl(InvoiceRepository invoiceRepository, SoundCarrierRepository soundCarrierRepository) throws RemoteException {
        this.soundCarrierRepository = soundCarrierRepository;
        this.invoiceRepository = invoiceRepository;
        this.sessionRepository = new ListSessionRepository();
    }

    @Override
    public void confirmPurchase(ShoppingCartDTO shoppingCartDTO, String sessionId)
            throws PurchaseFailedException, InvalidSessionException, RemoteException {

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
            Invoice invoice = createInvoice(shoppingCartDTO);
            invoiceRepository.add(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }

    public Invoice createInvoice(ShoppingCartDTO shoppingCartDTO) {
        // start with totalPrice of 0
        BigDecimal totalPrice = new BigDecimal(0);
        List<InvoiceLine> purchasedItems = new ArrayList<>();

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

        // get every item in shopping cart and calc their price
        // create a invoiceline with the information gathered from the shoppingcart item
        // calculate total price for invoice
        // key = articleId, value = quantity
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
