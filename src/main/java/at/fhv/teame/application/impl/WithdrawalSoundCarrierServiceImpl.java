package at.fhv.teame.application.impl;

import at.fhv.teame.domain.repositories.InvoiceRepository;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;
import at.fhv.teame.sharedlib.ejb.WithdrawSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.WithdrawalFailedException;

import javax.ejb.Stateless;
import java.util.Map;

@Stateless
public class WithdrawalSoundCarrierServiceImpl implements WithdrawSoundCarrierServiceRemote {

    private final SoundCarrierRepository soundCarrierRepository;
    private final InvoiceRepository invoiceRepository;
    public WithdrawalSoundCarrierServiceImpl(){
        this(new HibernateSoundCarrierRepository(), new HibernateInvoiceRepository());
    }

    public WithdrawalSoundCarrierServiceImpl(SoundCarrierRepository soundCarrierRepository, InvoiceRepository invoiceRepository){
        this.soundCarrierRepository = soundCarrierRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void withdrawSoundCarrier(String invoiceId, Map<String, Integer> soundCarrierReturnAmountMap) throws WithdrawalFailedException {
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
