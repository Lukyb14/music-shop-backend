package at.fhv.teame.application.impl;

import at.fhv.teame.rmi.client.RMIClient;
import at.fhv.teame.sharedlib.dto.CustomerDTO;
import at.fhv.teame.sharedlib.rmi.SearchCustomerService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SearchCustomerServiceImpl extends UnicastRemoteObject implements SearchCustomerService {

    private final SearchCustomerService searchCustomerService = RMIClient.getInstanceSearchCustomerService();

    public SearchCustomerServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public List<CustomerDTO> getCustomerByFullName(String givenName, String familyName, int pageNr) throws RemoteException {
        return searchCustomerService.getCustomerByFullName(givenName, familyName, pageNr);
    }

    @Override
    public List<CustomerDTO> getCustomerByFamilyName(String familyName, int pageNr) throws RemoteException {
        return searchCustomerService.getCustomerByFamilyName(familyName, pageNr);
    }

    @Override
    public int totResultsByFullName(String givenName, String familyName) throws RemoteException {
        return searchCustomerService.totResultsByFullName(givenName, familyName);
    }

    @Override
    public int totResultsByFamilyName(String familyName) throws RemoteException {
        return searchCustomerService.totResultsByFamilyName(familyName);
    }
}
