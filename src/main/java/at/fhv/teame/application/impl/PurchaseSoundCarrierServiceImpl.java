package at.fhv.teame.application.impl;


import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

public class PurchaseSoundCarrierServiceImpl extends UnicastRemoteObject implements PurchaseSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

    public PurchaseSoundCarrierServiceImpl() throws RemoteException { super(); }

    @Override
    public void confirmPurchase(Map<String, Integer> shoppingCartItems,
                                String paymentMethod,
                                BigDecimal totalPrice) throws PurchaseFailedException, RemoteException {
        if (shoppingCartItems.isEmpty()) {
            throw new PurchaseFailedException();
        }
        try {
            soundCarrierRepository.processPurchase(shoppingCartItems, paymentMethod);
            Invoice invoice = createInvoice(shoppingCartItems, paymentMethod, totalPrice);
            invoiceRepository.add(invoice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }

    public Invoice createInvoice(Map<String, Integer> shoppingCartItems,
                                  String paymentMethod,
                                  BigDecimal totalPrice) {

        Invoice invoice = new Invoice(LocalDate.now(), PaymentMethod.valueOf(paymentMethod.toUpperCase(Locale.ROOT)), totalPrice);

        for (Map.Entry<String, Integer> entry : shoppingCartItems.entrySet()) {
            SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(entry.getKey());

            InvoiceLine invoiceLine = new InvoiceLine(invoice, soundCarrier, entry.getValue(), soundCarrier.getPrice());
            invoice.addInvoiceItem(invoiceLine);
        }
        return invoice;
    }
}
