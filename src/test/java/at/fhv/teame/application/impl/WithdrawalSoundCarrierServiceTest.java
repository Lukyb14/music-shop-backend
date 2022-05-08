package at.fhv.teame.application.impl;

import at.fhv.teame.mocks.MockInvoiceRepository;
import at.fhv.teame.mocks.MockSessionRepository;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.ejb.WithdrawSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.rmi.WithdrawSoundCarrierService;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.WithdrawalFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawalSoundCarrierServiceTest {
    private WithdrawSoundCarrierServiceRemote withdrawSoundCarrierService;

    @BeforeEach
    void setup() {
        withdrawSoundCarrierService = new WithdrawalSoundCarrierServiceImpl(
                new MockSoundCarrierRepository(),
                new MockInvoiceRepository(),
                new MockSessionRepository()
        );
    }

    @Test
    void withdrawSoundCarrier() throws InvalidSessionException, WithdrawalFailedException {
        String invoiceId = "20000";
        Map<String, Integer> returnedSoundCarriers = Map.of(
                "1000", 2,
                "210", 1,
                "7381", 3
        );

        withdrawSoundCarrierService.withdrawSoundCarrier(invoiceId, returnedSoundCarriers, UUID.randomUUID().toString());
    }

    @Test
    void given_withdrawSoundCarrier_when_mapEmpty_then_throwException() {
        String invoiceId = "20000";
        Map<String, Integer> returnedSoundCarriers = Map.of();

        assertThrows(WithdrawalFailedException.class, () -> withdrawSoundCarrierService.withdrawSoundCarrier(invoiceId, returnedSoundCarriers, UUID.randomUUID().toString()));
    }
}
