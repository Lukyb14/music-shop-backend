package at.fhv.teame;

import at.fhv.teame.application.impl.MessagingServiceImpl;
import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.rmi.RMIClient;
import at.fhv.teame.rmi.RMIFactoryImpl;
import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.rmi.exceptions.PublishingFailedException;

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
            MessagingServiceImpl messagingService = new MessagingServiceImpl();
            PublishMessageDTO publishMessageDTO = new PublishMessageDTO("System.Message","Annanas", "Dies das!");
            messagingService.publishMessage(publishMessageDTO, "1");
            //messagingService.receiveMessage();
        } catch (RemoteException | MalformedURLException | PublishingFailedException e) {
            e.printStackTrace();
        }
    }
}
