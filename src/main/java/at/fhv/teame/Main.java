package at.fhv.teame;

import at.fhv.teame.application.impl.SoundCarrierServiceImpl;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            SoundCarrierServiceImpl soundCarrierService = SoundCarrierServiceImpl.getInstance();
            Naming.rebind("rmi://localhost/soundCarrierService", soundCarrierService);
            System.out.println("soundCarrierService bound in registry");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
