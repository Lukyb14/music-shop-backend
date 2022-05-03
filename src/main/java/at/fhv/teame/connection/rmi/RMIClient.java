package at.fhv.teame.connection.rmi;

import at.fhv.teame.sharedlib.rmi.CustomerService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {
    private static CustomerService searchCustomerServiceStub;

    private RMIClient() {}

    public static void startRMIClient() {
        try {
            searchCustomerServiceStub = (CustomerService) Naming.lookup("rmi://localhost:1099/searchCustomerService");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public static CustomerService getInstanceCustomerService() {
        if(searchCustomerServiceStub == null) {
            startRMIClient();
            return searchCustomerServiceStub;
        }
        return searchCustomerServiceStub;
    }
}
