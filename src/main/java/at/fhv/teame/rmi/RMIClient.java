package at.fhv.teame.rmi;

import at.fhv.teame.application.impl.SearchCustomerServiceImpl;
import at.fhv.teame.sharedlib.rmi.SearchCustomerService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    private static SearchCustomerService searchCustomerServiceStub;

    private RMIClient() {}

    public static void startRMIClient() {
        try {
            searchCustomerServiceStub = (SearchCustomerService) Naming.lookup("rmi://localhost:1099/searchCustomerService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static SearchCustomerService getInstanceSearchCustomerService() {
        if(searchCustomerServiceStub == null) {
            startRMIClient();
            return searchCustomerServiceStub;
        }
        return searchCustomerServiceStub;
    }
}
