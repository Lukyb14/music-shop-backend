package at.fhv.teame.application.impl;


import at.fhv.teame.domain.PaymentMethod;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.ShoppingCartItemDTO;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseSoundCarrierServiceImpl implements PurchaseSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository;

    public PurchaseSoundCarrierServiceImpl()  {
        this.soundCarrierRepository = HibernateSoundCarrierRepository.getInstance();
    }

    @Override
    public void confirmPurchase(List<ShoppingCartItemDTO> shoppingCartItemDtos, String paymentMethod) throws PurchaseFailedException {
        if (shoppingCartItemDtos.isEmpty()) {
            throw new PurchaseFailedException();
        }
        try {
            PaymentMethod paymentMethodEnum = PaymentMethod.valueOf(paymentMethod);

            Map<SoundCarrier, Integer> items = new HashMap<>();
            for (ShoppingCartItemDTO dto : shoppingCartItemDtos) {
                SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(dto.getArticleId());
                // add soundcarrier with its amount to the map and if the key-value pair already exists add them together
                items.compute(soundCarrier, (sc, amt) -> (amt == null) ? dto.getAmount() : amt + dto.getAmount());
            }

            System.out.println("Payment method: " + paymentMethodEnum);
            soundCarrierRepository.retrieveSoundCarriers(items);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }
}
