package at.fhv.teame.application.impl;


import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.rmi.RMIClient;
import at.fhv.teame.rmi.Session;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.rmi.CustomerService;
import at.fhv.teame.sharedlib.rmi.SearchCustomerService;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.UUID;

public class SearchCustomerServiceImpl extends UnicastRemoteObject implements SearchCustomerService {

    private final CustomerService searchCustomerService;
    private final SessionRepository sessionRepository;

    public SearchCustomerServiceImpl() throws RemoteException {
        this(RMIClient.getInstanceCustomerService(), new ListSessionRepository());
    }

    public SearchCustomerServiceImpl(CustomerService customerService, SessionRepository sessionRepository) throws RemoteException {
        this.searchCustomerService = customerService;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public List<CustomerDTO> getCustomerByFullName(String givenName, String familyName, int pageNr, String sessionId) throws RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerService.getCustomerByFullName(givenName, familyName, pageNr);
    }

    @Override
    public List<CustomerDTO> getCustomerByFamilyName(String familyName, int pageNr, String sessionId) throws RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerService.getCustomerByFamilyName(familyName, pageNr);
    }

    @Override
    public int totResultsByFullName(String givenName, String familyName, String sessionId) throws RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerService.totResultsByFullName(givenName, familyName);
    }

    @Override
    public int totResultsByFamilyName(String familyName, String sessionId) throws RemoteException, InvalidSessionException {
        try {
            Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!session.isSeller()) throw new InvalidSessionException();
        } catch (SessionNotFoundException ignored) {
            throw new InvalidSessionException();
        }

        return searchCustomerService.totResultsByFamilyName(familyName);
    }
}
