package at.fhv.teame.domain.repositories;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.session.Session;

import javax.ejb.Local;
import java.util.UUID;
@Local
public interface SessionRepository {
    Session createSession(ClientUser clientUser);
    Session sessionById(UUID sessionId) throws SessionNotFoundException;
    void deleteSession(UUID sessionId);
}
