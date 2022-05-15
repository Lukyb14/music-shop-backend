package at.fhv.teame.application.impl;


import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.domain.model.invoice.PaymentMethod;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class PurchaseSoundCarrierServiceImpl implements PurchaseSoundCarrierServiceRemote {

    @EJB
    private SoundCarrierRepository soundCarrierRepository;
    @EJB
    private InvoiceRepository invoiceRepository;

    // default constructor with hibernate
    public PurchaseSoundCarrierServiceImpl() { }

    // for mocking
    public PurchaseSoundCarrierServiceImpl(InvoiceRepository invoiceRepository, SoundCarrierRepository soundCarrierRepository){
        this.soundCarrierRepository = soundCarrierRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void confirmPurchase(ShoppingCartDTO shoppingCartDTO) throws PurchaseFailedException {
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
