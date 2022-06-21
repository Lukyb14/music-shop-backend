package at.fhv.teame.infrastructure;

import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.UserRepository;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
@Stateless
public class HibernateUserRepository implements UserRepository {

    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");

    private EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public ClientUser userByCn(String cn) throws UserNotFoundException {
        ClientUser clientUser = entityManager.find(ClientUser.class, cn);

        if (clientUser == null) {
            throw new UserNotFoundException();
        } else {
            return clientUser;
        }
    }

    @Override
    public List<String> allTopics() {
        return entityManager.createNativeQuery("SELECT DISTINCT topics FROM clientuser_topics WHERE topics != 'Order' ORDER BY topics;").getResultList();
    }
}
