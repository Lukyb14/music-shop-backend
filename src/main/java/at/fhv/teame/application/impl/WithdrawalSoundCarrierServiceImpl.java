package at.fhv.teame.application.impl;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.rmi.Session;
import at.fhv.teame.sharedlib.rmi.WithdrawSoundCarrierService;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.WithdrawalFailedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.UUID;

public class WithdrawalSoundCarrierServiceImpl extends UnicastRemoteObject implements WithdrawSoundCarrierService {

    private final SoundCarrierRepository soundCarrierRepository;
    private final InvoiceRepository invoiceRepository;
    private final SessionRepository sessionRepository;

    public WithdrawalSoundCarrierServiceImpl() throws RemoteException {
        this(new HibernateSoundCarrierRepository(), new HibernateInvoiceRepository(), new ListSessionRepository());
    }

    public WithdrawalSoundCarrierServiceImpl(SoundCarrierRepository soundCarrierRepository, InvoiceRepository invoiceRepository, SessionRepository sessionRepository) throws RemoteException {
        this.soundCarrierRepository = soundCarrierRepository;
        this.invoiceRepository = invoiceRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void withdrawSoundCarrier(String invoiceId, Map<String, Integer> soundCarrierReturnAmountMap, String sessionId) throws WithdrawalFailedException, RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }
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
