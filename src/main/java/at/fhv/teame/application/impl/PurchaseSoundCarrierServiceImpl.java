package at.fhv.teame.application.impl;


import at.fhv.teame.domain.PaymentMethod;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PurchaseSoundCarrierServiceImpl extends UnicastRemoteObject implements PurchaseSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository;

    public PurchaseSoundCarrierServiceImpl() throws RemoteException {
        this.soundCarrierRepository = HibernateSoundCarrierRepository.getInstance();
    }

    @Override
    public void confirmPurchase(Map<String, Integer> shoppingCartItems, String paymentMethod) throws PurchaseFailedException, RemoteException {
        if (shoppingCartItems.isEmpty()) {
            throw new PurchaseFailedException();
        }
        try {
            PaymentMethod paymentMethodEnum = PaymentMethod.valueOf(paymentMethod.toUpperCase(Locale.ROOT));

            Map<SoundCarrier, Integer> items = new HashMap<>();
            for (Map.Entry<String, Integer> entry : shoppingCartItems.entrySet()) {
                SoundCarrier soundCarrier = soundCarrierRepository.soundCarrierByArticleId(entry.getKey());
                items.put(soundCarrier, entry.getValue());
            }

            System.out.println("Payment method: " + paymentMethodEnum);
            soundCarrierRepository.retrieveSoundCarriers(items);

        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }
}
