package at.fhv.teame.application.impl;

import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.WithdrawalDTO;
import at.fhv.teame.sharedlib.rmi.WithdrawSoundCarrierService;
import at.fhv.teame.sharedlib.rmi.exceptions.WithdrawalFailedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class WithDrawSoundCarrierServiceImpl extends UnicastRemoteObject implements WithdrawSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
    private final InvoiceRepository invoiceRepository = new HibernateInvoiceRepository();

    public WithDrawSoundCarrierServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void withDrawSoundCarrier(WithdrawalDTO withdrawalDTO)
            throws WithdrawalFailedException, RemoteException {
        if (withdrawalDTO.getSoundCarrierWithAmount().isEmpty()) {
            throw new WithdrawalFailedException();
        }
        try {
            soundCarrierRepository.processWithdrawal(withdrawalDTO);
            for (Map.Entry<String, Integer> entry : withdrawalDTO.getSoundCarrierWithAmount().entrySet()) {
                if (entry.getValue() > 0) {
                    invoiceRepository.updateInvoiceLine(
                            Long.valueOf(withdrawalDTO.getInvoiceId()),
                            entry.getKey(),
                            entry.getValue()
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new WithdrawalFailedException();
        }
    }
}
