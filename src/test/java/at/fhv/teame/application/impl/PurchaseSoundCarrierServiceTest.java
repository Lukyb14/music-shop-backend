package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;
import at.fhv.teame.mocks.MockInvoiceRepository;
import at.fhv.teame.mocks.MockSessionRepository;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseSoundCarrierServiceTest {

    static PurchaseSoundCarrierServiceImpl purchaseSoundCarrierService;

    @BeforeAll
    static void beforeAll() throws RemoteException {
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl(new MockInvoiceRepository(), new MockSoundCarrierRepository(), new MockSessionRepository());
    }

    @Test
    void given_ShoppingCartDtoAndPurchasedItemsIsEmpty_when_confirmPurchase_then_PurchaseFailedException() throws RemoteException, PurchaseFailedException {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        //expectedPurchasedItems.put("100001", 1);
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();

        //then
        assertThrows(PurchaseFailedException.class, () -> {
            purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO, UUID.randomUUID().toString()); // TODO add session uuid here
        });
    }

    @Test
    void given_ShoppingCartDtoAndPurchasedItems_when_confirmPurchase_then_NoException() throws RemoteException, PurchaseFailedException, InvalidSessionException {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", 1);
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();

        //when
        purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO, UUID.randomUUID().toString());

        //then
        assertDoesNotThrow(PurchaseFailedException::new);
    }

    @Test
    void given_ShoppingCartDtoWithExistingGuest_when_confirmPurchase_then_InvoiceCreated() {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", 1);
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();

        //when
        Invoice invoiceActual = purchaseSoundCarrierService.createInvoice(shoppingCartDTO);
        InvoiceLine invoiceLineExpected = new InvoiceLine(
                invoiceActual.getPurchasedItems().get(0).getInvoice(),
                invoiceActual.getPurchasedItems().get(0).getSoundCarrier(),
                invoiceActual.getPurchasedItems().get(0).getQuantity(),
                invoiceActual.getPurchasedItems().get(0).getPrice());

        //then
        assertEquals(invoiceLineExpected.getInvoice(), invoiceActual.getPurchasedItems().get(0).getInvoice());
        assertEquals(invoiceLineExpected.getSoundCarrier(), invoiceActual.getPurchasedItems().get(0).getSoundCarrier());
        assertEquals(invoiceLineExpected.getPrice(), invoiceActual.getPurchasedItems().get(0).getPrice());
        assertEquals(invoiceLineExpected.getQuantity(), invoiceActual.getPurchasedItems().get(0).getQuantity());
        assertEquals(LocalDate.now(), invoiceActual.getDateOfPurchase());
        assertEquals(shoppingCartDTO.getCustomerAddress(), invoiceActual.getCustomerAddress());
        assertEquals(shoppingCartDTO.getCustomerFirstName(), invoiceActual.getCustomerFirstName());
        assertEquals(shoppingCartDTO.getCustomerLastName(), invoiceActual.getCustomerLastName());
        assertEquals(shoppingCartDTO.getPaymentMethod(), invoiceActual.getPaymentMethod().toString());
        assertEquals(BigDecimal.valueOf(20).setScale(2,  RoundingMode.HALF_UP), invoiceActual.getToRefund());
    }

    @Test
    void given_ShoppingCartDtoWithNonExistingGuest_when_createInvoice_then_InvoiceCreated() {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", 1);
        ShoppingCartDTO shoppingCartDTO = ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "guest",
                        "",
                        "")
                .build();

        //when
        Invoice invoiceActual = purchaseSoundCarrierService.createInvoice(shoppingCartDTO);
        InvoiceLine invoiceLineExpected = new InvoiceLine(
                invoiceActual.getPurchasedItems().get(0).getInvoice(),
                invoiceActual.getPurchasedItems().get(0).getSoundCarrier(),
                invoiceActual.getPurchasedItems().get(0).getQuantity(),
                invoiceActual.getPurchasedItems().get(0).getPrice());

        //then
        assertEquals(invoiceLineExpected.getInvoice(), invoiceActual.getPurchasedItems().get(0).getInvoice());
        assertEquals(invoiceLineExpected.getSoundCarrier(), invoiceActual.getPurchasedItems().get(0).getSoundCarrier());
        assertEquals(invoiceLineExpected.getPrice(), invoiceActual.getPurchasedItems().get(0).getPrice());
        assertEquals(invoiceLineExpected.getQuantity(), invoiceActual.getPurchasedItems().get(0).getQuantity());
        assertEquals(LocalDate.now(), invoiceActual.getDateOfPurchase());
        assertEquals("guest", invoiceActual.getCustomerFirstName());
        assertEquals(shoppingCartDTO.getPaymentMethod(), invoiceActual.getPaymentMethod().toString());
        assertEquals(BigDecimal.valueOf(20).setScale(2,  RoundingMode.HALF_UP), invoiceActual.getToRefund());

    }
}
