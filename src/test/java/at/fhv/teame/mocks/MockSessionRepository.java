package at.fhv.teame.mocks;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.model.user.Role;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.rmi.Session;

import java.lang.reflect.Field;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class MockSessionRepository implements SessionRepository {

    private Session session = new Session((new ClientUser("lbo3144", "Lukas", "Boch", Role.ADMINISTRATOR)));

    @Override
    public Session createSession(ClientUser clientUser) {
        session = new Session(clientUser);
        return session;
    }

    @Override
    public Session sessionById(UUID sessionId) throws SessionNotFoundException {
        if(sessionId.toString().equals("b16c5200-bb0e-11ec-8422-0242ac120002")) {
            throw new SessionNotFoundException();
        } else if (sessionId.toString().equals("b16c5200-bb0e-11ec-8422-0242ac120003")) {
            ClientUser lukas = new ClientUser("lbo3144", "Lukas", "Boch", Role.OPERATOR);
            Session session1 = new Session(lukas);

            try {
                List<String> topicList = List.of("Rock", "Order", "System.Message");

                Field topics = ClientUser.class.getDeclaredField("topics");
                topics.setAccessible(true);
                topics.set(lukas, topicList);

                return session1;
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new SessionNotFoundException();
            }

        }
        return session;
    }

    @Override
    public void deleteSession(UUID sessionId) {

    }
}
