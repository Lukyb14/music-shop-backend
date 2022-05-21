package at.fhv.teame.integration.middleware;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.middleware.AuthenticationServiceImpl;
import at.fhv.teame.middleware.api.AuthenticationServiceLocal;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AuthenticationServiceImplTest {
    static AuthenticationServiceRemote authenticationService;

    static AuthenticationServiceLocal authenticationServiceLocal;

    static ListSessionRepository listSessionRepository;

    @BeforeEach
    void setup() {
        listSessionRepository = new ListSessionRepository();
        authenticationService = new AuthenticationServiceImpl(new HibernateUserRepository(), listSessionRepository);
        authenticationServiceLocal = new AuthenticationServiceImpl();
    }

    @Test
    void given_credentials_when_login_then_no_exception() {
        //given
        String username = "har9090";
        String password = "PssWrd";

        //when ..then
        assertDoesNotThrow(() -> authenticationService.login(username, password));
    }

    @Test
    void given_invalid_password_when_login_then_loginFailed() {
        //given
        String username = "har9090";
        String password = "wrong";

        //when ..then
        assertThrows(LoginFailedException.class, () -> authenticationService.login(username, password));
    }

    @Test
    void given_invalid_username_and_password_when_login_then_loginFailed() {
        //given
        String username = "xy283";
        String password = "wrong";

        //when ..then
        assertThrows(LoginFailedException.class, () -> authenticationService.login(username, password));
    }

    @Test
    void given_credentials_when_loginCustomer_then_no_exception() {
        //given
        String username = "jost97";
        String password = "josibosi";

        //when ..then
        assertDoesNotThrow(() -> authenticationServiceLocal.loginCustomer(username, password));
    }

    @Test
    void given_invalid_password_when_loginCustomer_then_loginFailed() {
        //given
        String username = "jost97";
        String password = "wrong";

        //when ..then
        assertThrows(LoginFailedException.class, () -> authenticationServiceLocal.loginCustomer(username, password));
    }

    @Test
    void given_invalid_username_and_password_when_loginCustomer_then_loginFailed() {
        //given
        String username = "jost99";
        String password = "wrong";

        //when ..then
        assertThrows(LoginFailedException.class, () -> authenticationServiceLocal.loginCustomer(username, password));
    }

    @Test
    void given_sessionId_when_logout_and_sessionbyId_then_throws_sessionNotFound() {
        //given
        String username = "har9090";
        String password = "PssWrd";


        String sessionId = null;
        try {
            sessionId = authenticationService.login(username, password).getSessionId();
        } catch (LoginFailedException e) {
            throw new RuntimeException(e);
        }

        //when
        authenticationService.logout(sessionId);

        //then
        String finalSessionId = sessionId;
        assertThrows(SessionNotFoundException.class, () -> listSessionRepository.sessionById(UUID.fromString(finalSessionId)));
    }
}
