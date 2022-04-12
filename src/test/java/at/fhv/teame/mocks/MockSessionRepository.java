package at.fhv.teame.mocks;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.model.user.Role;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.infrastructure.HibernateInvoiceRepository;
import at.fhv.teame.rmi.Session;

import java.util.UUID;

public class MockSessionRepository implements SessionRepository {
    @Override
    public Session createSession(ClientUser clientUser) {
        return new Session(clientUser);
    }

    @Override
    public Session sessionById(UUID sessionId) throws SessionNotFoundException {
        return new Session(new ClientUser("lbo3144", "Lukas", "Boch", Role.ADMINISTRATOR));
    }

    @Override
    public void deleteSession(UUID sessionId) {

    }
}
