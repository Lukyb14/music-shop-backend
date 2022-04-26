package at.fhv.teame.mocks;

import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.UserRepository;

import java.util.List;

public class MockUserRepository implements UserRepository {
    @Override
    public ClientUser userByCn(String cn) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<String> allTopics() {
        return null;
    }
}
