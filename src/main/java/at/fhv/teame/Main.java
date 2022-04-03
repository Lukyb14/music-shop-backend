package at.fhv.teame;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.rmi.RMIFactoryImpl;
import at.fhv.teame.rmi.client.RMIClient;
import at.fhv.teame.sharedlib.rmi.SearchSoundCarrierService;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            RMIFactoryImpl rmiFactory = new RMIFactoryImpl();
            Naming.rebind("rmi://localhost/rmiFactory", rmiFactory);
            System.out.println("rmiFactory bound in registry");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
