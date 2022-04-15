package at.fhv.teame.infrastructure;

import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class HibernateUserRepository implements UserRepository {

    private final EntityManagerFactory entityManagerFactory;

    public HibernateUserRepository() {
        this.entityManagerFactory = Persistence.createEntityManagerFactory("at.fhv.teame");
    }

    @Override
    public ClientUser userByCn(String cn) throws UserNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        ClientUser clientUser = entityManager.find(ClientUser.class, cn);

        if (clientUser == null) {
            throw new UserNotFoundException();
        } else {
            return clientUser;
        }
    }

    @Override
    public List<String> allTopics() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.createNativeQuery("SELECT DISTINCT topics FROM clientuser_topics ORDER BY topics;").getResultList();
    }
}
