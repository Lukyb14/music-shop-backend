package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.session.Session;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.model.user.Role;
import at.fhv.teame.mocks.MockSessionRepository;
import at.fhv.teame.sharedlib.ejb.SessionValidationRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionValidationImplTest {

    private SessionValidationRemote sessionValidation;

    private MockSessionRepository mockSessionRepository;


    @BeforeEach
    void beforeEach() {
        mockSessionRepository = new MockSessionRepository();
        sessionValidation = new SessionValidationImpl(mockSessionRepository);
    }

    @Test
    void given_validsession_when_validateAny_then_not_throws() {
        //given
        Session sessionId = mockSessionRepository.createSession(new ClientUser("har9090", "Hüseyin", "Arziman", Role.ADMINISTRATOR));

        //when .. then
        assertDoesNotThrow(() -> sessionValidation.validateAny(sessionId.getSessionId().toString()));
    }


    @Test
    void given_invalidsessionid_when_validateAny_then_throws_invalidSession() {
        //given
        String wrongSessionId = "1278309213";

        //when .. then
        assertThrows(InvalidSessionException.class, () -> sessionValidation.validateAny(wrongSessionId));
    }

    @Test
    void given_validsession_when_validateSeller_then_not_throws() {
        //given
        Session sessionId = mockSessionRepository.createSession(new ClientUser("har9090", "Hüseyin", "Arziman", Role.SELLER));

        //when .. then
        assertDoesNotThrow(() -> sessionValidation.validateSeller(sessionId.getSessionId().toString()));
    }


    @Test
    void given_validsession_with_role_operator_when_validateSeller_then_not_throws() {
        //given
        Session sessionId = mockSessionRepository.createSession(new ClientUser("har9090", "Hüseyin", "Arziman", Role.OPERATOR));

        //when .. then
        assertThrows(InvalidSessionException.class, () -> sessionValidation.validateSeller(sessionId.getSessionId().toString()));
    }


    @Test
    void given_invalidsessionid_when_validateSeller_then_throws_invalidSession() {
        //given
        String wrongSessionId = "1278309213";

        //when .. then
        assertThrows(InvalidSessionException.class, () -> sessionValidation.validateAny(wrongSessionId));
    }

    @Test
    void given_validsession_when_validateOperator_then_not_throws() {
        //given
        Session sessionId = mockSessionRepository.createSession(new ClientUser("har9090", "Hüseyin", "Arziman", Role.OPERATOR));

        //when .. then
        assertDoesNotThrow(() -> sessionValidation.validateOperator(sessionId.getSessionId().toString()));
    }


    @Test
    void given_validsession_with_role_seller_when_validateOperator_then_throws_invalidSession() {
        //given
        Session sessionId = mockSessionRepository.createSession(new ClientUser("har9090", "Hüseyin", "Arziman", Role.SELLER));

        //when .. then
        assertThrows(InvalidSessionException.class, () -> sessionValidation.validateOperator(sessionId.getSessionId().toString()));
    }


    @Test
    void given_invalidsessionid_when_validateOperator_then_throws_invalidSession() {
        //given
        String wrongSessionId = "1278309213";

        //when .. then
        assertThrows(InvalidSessionException.class, () -> sessionValidation.validateOperator(wrongSessionId));
    }
}