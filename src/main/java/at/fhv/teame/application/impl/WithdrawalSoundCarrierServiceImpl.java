package at.fhv.teame.application.impl;

import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.rmi.WithdrawSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.WithdrawalFailedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class WithdrawalSoundCarrierServiceImpl extends UnicastRemoteObject implements WithdrawSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

    public WithdrawalSoundCarrierServiceImpl() throws RemoteException {
        super();
    }


    @Override
    public void withdrawSoundCarrier(String invoiceId, Map<String, Integer> soundCarrierReturnAmountMap) throws WithdrawalFailedException, RemoteException {
        if (soundCarrierReturnAmountMap.isEmpty()) {
            throw new WithdrawalFailedException();
        }

        try {
            for (Map.Entry<String, Integer> entry : soundCarrierReturnAmountMap.entrySet()) {
                //If article has returned items
                if (entry.getValue() > 0) {
                    soundCarrierRepository.fillStock(entry.getKey(), entry.getValue());
                    invoiceRepository.updateInvoiceLine(invoiceId, entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WithdrawalFailedException();
        }
    }

}
