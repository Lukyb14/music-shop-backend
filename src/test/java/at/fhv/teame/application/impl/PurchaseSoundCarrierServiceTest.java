package at.fhv.teame.application.impl;

import at.fhv.teame.mocks.MockInvoiceRepository;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PurchaseSoundCarrierServiceTest {

    private PurchaseSoundCarrierServiceRemote purchaseSoundCarrierService;

    @BeforeEach
    void beforeEach() {
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl(new MockInvoiceRepository(), new MockSoundCarrierRepository());
    }

    @Test
    void given_ShoppingCartDtoAndPurchasedItemsIsEmpty_when_confirmPurchase_then_throws() {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        ShoppingCartDTO shoppingCartDTO =  ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();

        //when..then
        assertThrows(PurchaseFailedException.class, () -> {
            purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);
        });
    }

    @Test
    void given_ShoppingCartDtoWithInvalidAmount_when_confirmPurchase_then_throws() {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", -1);
        ShoppingCartDTO shoppingCartDTO =  ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();

        //when..then
        assertThrows(PurchaseFailedException.class, () -> {
            purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);
        }, "PurchaseFailedException was expected");
    }

    @Test
    void given_ShoppingCartDtAndGuest_when_confirmPurchase_then_NoException() throws PurchaseFailedException {
        //given
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", 1);
        ShoppingCartDTO shoppingCartDTO =  ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        null,
                        "Guest",
                        null)
                .build();

        //when
        purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);

        //then
        assertDoesNotThrow(PurchaseFailedException::new);
    }

    @Test
    void given_ShoppingCartDtoAndPurchasedItems_when_confirmPurchase_then_NoException() throws PurchaseFailedException {
        //given
        ShoppingCartDTO shoppingCartDTO = buildShoppingCartDto();

        //when
        purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);

        //then
        assertDoesNotThrow(PurchaseFailedException::new);
    }


    private ShoppingCartDTO buildShoppingCartDto() {
        HashMap<String, Integer> expectedPurchasedItems = new HashMap<>();
        expectedPurchasedItems.put("100001", 1);
        return ShoppingCartDTO.builder()
                .withShoppingCartEntity(
                        expectedPurchasedItems,
                        "CASH",
                        "Umut Can",
                        "Caglayan",
                        "Hochschulstrasse 1")
                .build();
    }
}
