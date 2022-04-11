package at.fhv.teame;

import at.fhv.teame.rmi.RMIClient;
import at.fhv.teame.rmi.RMIFactoryImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class Main {
    public static void main(String[] args) {
        try {
            RMIClient.startRMIClient();
            LocateRegistry.createRegistry(1100);
            RMIFactoryImpl rmiFactory = new RMIFactoryImpl();
            Naming.rebind("rmi://localhost:1100/rmiFactory", rmiFactory);
            System.out.println("rmiFactory bound in registry");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
