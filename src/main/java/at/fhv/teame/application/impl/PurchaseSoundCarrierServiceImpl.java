package at.fhv.teame.application.impl;


import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.PurchaseFailedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class PurchaseSoundCarrierServiceImpl extends UnicastRemoteObject implements PurchaseSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();;

    public PurchaseSoundCarrierServiceImpl() throws RemoteException { super(); }

    @Override
    public void confirmPurchase(Map<String, Integer> shoppingCartItems, String paymentMethod) throws PurchaseFailedException, RemoteException {
        if (shoppingCartItems.isEmpty()) {
            throw new PurchaseFailedException();
        }
        try {
            soundCarrierRepository.processPurchase(shoppingCartItems, paymentMethod);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PurchaseFailedException();
        }
    }
}
