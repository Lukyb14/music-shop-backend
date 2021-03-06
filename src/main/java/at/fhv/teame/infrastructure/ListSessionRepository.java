package at.fhv.teame.infrastructure;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.model.session.Session;

import javax.ejb.Stateless;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Stateless
public class ListSessionRepository implements SessionRepository {

    private static final Set<Session> sessions = new HashSet<>();

    @Override
    public Session createSession(ClientUser clientUser) {

        Session newSession = new Session(clientUser);
        sessions.add(newSession);

        return newSession;
    }

    @Override
    public Session sessionById(UUID sessionId) throws SessionNotFoundException {

        return sessions.stream()
                .filter(session -> session.getSessionId().equals(sessionId))
                .findFirst()
                .orElseThrow(SessionNotFoundException::new);
    }

    @Override
    public void deleteSession(UUID sessionId) {
        sessions.stream()
                .filter(session -> session.getSessionId().equals(sessionId))
                .findFirst()
                .ifPresent(session -> sessions.remove(session));
    }
}
