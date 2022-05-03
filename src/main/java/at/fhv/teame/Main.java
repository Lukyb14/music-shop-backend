package at.fhv.teame;

import at.fhv.teame.connection.ejb.EJBClient;
import at.fhv.teame.connection.rmi.RMIClient;
import at.fhv.teame.connection.rmi.RMIFactoryImpl;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

@Startup
@Singleton
public class Main {
    @PostConstruct
    public static void main(String[] args) {
        try {
            //RMIClient.startRMIClient();
            EJBClient.initialEJB();
            LocateRegistry.createRegistry(1100);
            RMIFactoryImpl rmiFactory = new RMIFactoryImpl();
            Naming.rebind("rmi://localhost:1100/rmiFactory", rmiFactory);
            System.out.println("Backend started");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
