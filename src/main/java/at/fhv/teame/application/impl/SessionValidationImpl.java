package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.session.Session;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.sharedlib.ejb.SessionValidationRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.UUID;

@Stateless
public class SessionValidationImpl implements SessionValidationRemote {

    @EJB
    private SessionRepository sessionRepository;

    public SessionValidationImpl() { }

    //for mocking
    public SessionValidationImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void validateAny(String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if(session.isExpired()) throw new InvalidSessionException();
        } catch (Exception ignored) {
            throw new InvalidSessionException();
        }
    }

    @Override
    public void validateSeller(String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller() || session.isExpired()) throw new InvalidSessionException();
        } catch (Exception ignored) {
            throw new InvalidSessionException();
        }
    }

    @Override
    public void validateOperator(String sessionId) throws InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isOperator() || session.isExpired()) throw new InvalidSessionException();
        } catch (Exception ignored) {
            throw new InvalidSessionException();
        }
    }

}
