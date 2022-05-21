package at.fhv.teame.middleware.api;

import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;

import javax.ejb.Local;

@Local
public interface AuthenticationServiceLocal {
    String loginCustomer(String username, String password) throws LoginFailedException;
}
