package at.fhv.teame.connection.rmi;

import at.fhv.teame.application.impl.*;
import at.fhv.teame.sharedlib.rmi.*;
import at.fhv.teame.sharedlib.rmi.factory.RMIFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RMIFactoryImpl extends UnicastRemoteObject implements RMIFactory {

    public RMIFactoryImpl() throws RemoteException {
        super();
    }

    @Override
    public SearchSoundCarrierService createSearchSoundCarrierServiceImpl() throws RemoteException {
        return new SearchSoundCarrierServiceImpl();
    }

    @Override
    public PurchaseSoundCarrierService createPurchaseSoundCarrierServiceImpl() throws RemoteException {
        return new PurchaseSoundCarrierServiceImpl();
    }

    @Override
    public SearchCustomerService createSearchCustomerServiceImpl() throws RemoteException {
        return new SearchCustomerServiceImpl();
    }

    @Override
    public SearchInvoiceService createSearchInvoiceServiceImpl() throws RemoteException {
        return new SearchInvoiceServiceImpl();
    }

    @Override
    public WithdrawSoundCarrierService createWithdrawSoundCarrierServiceImpl() throws RemoteException {
        return new WithdrawalSoundCarrierServiceImpl();
    }

    @Override
    public MessageService createMessageServiceImpl() throws RemoteException {
        return new MessagingServiceImpl();
    }

    @Override
    public AuthenticationService createSearchAuthenticationServiceImpl() throws RemoteException {
        return new AuthenticationServiceImpl();
    }
}
