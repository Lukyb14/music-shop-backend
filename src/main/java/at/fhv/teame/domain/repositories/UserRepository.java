package at.fhv.teame.domain.repositories;

import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;

public interface UserRepository {
    ClientUser userByCn(String cn) throws UserNotFoundException;
}
