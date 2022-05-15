package at.fhv.teame.domain.repositories;

import at.fhv.teame.application.exceptions.UserNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;

import javax.ejb.Local;
import java.util.List;
@Local
public interface UserRepository {
    ClientUser userByCn(String cn) throws UserNotFoundException;
    List<String> allTopics();
}
