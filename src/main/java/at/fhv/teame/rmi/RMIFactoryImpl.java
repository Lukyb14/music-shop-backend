package at.fhv.teame.rmi;

import at.fhv.teame.application.impl.PurchaseSoundCarrierServiceImpl;
import at.fhv.teame.application.impl.SearchCustomerServiceImpl;
import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.rmi.PurchaseSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.SearchCustomerService;
import at.fhv.teame.sharedlib.rmi.SearchSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.factory.RMIFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIFactoryImpl extends UnicastRemoteObject implements RMIFactory {

    public RMIFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public SearchSoundCarrierService createSearchSoundCarrierService() throws RemoteException {
        return new SearchSoundCarrierServiceImpl();
    }

    @Override
    public PurchaseSoundCarrierService createPurchaseSoundCarrierService() throws RemoteException {
        return new PurchaseSoundCarrierServiceImpl();
    }

    @Override
    public SearchCustomerService createSearchCustomerServiceImpl() throws RemoteException {
        return new SearchCustomerServiceImpl();
    }

}
