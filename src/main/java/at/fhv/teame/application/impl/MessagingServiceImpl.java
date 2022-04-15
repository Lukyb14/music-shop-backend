package at.fhv.teame.application.impl;

import org.apache.activemq.ActiveMQConnectionFactory;
import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.rmi.MessageService;
import at.fhv.teame.sharedlib.rmi.exceptions.PublishingFailedException;

import javax.jms.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MessagingServiceImpl extends UnicastRemoteObject implements MessageService {


    public MessagingServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void publishMessage(PublishMessageDTO publishMessageDTO, String sessionId) throws RemoteException, PublishingFailedException {
        String url = "tcp://10.0.40.169:61616";
        try {
            // Get the ConnectionFactory by JNDI name
            ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(url);
            // Create a connection
            Connection con = cf.createConnection();
            // create a JMS session
            Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = sess.createTopic(publishMessageDTO.getTopic());
            // Create some Message and a MessageProducer with the session
            Message msg = sess.createTextMessage(publishMessageDTO.getContent());
            MessageProducer prod = sess.createProducer(dest);
            // send the message to the destination
            prod.send(msg);
            // Close the connection
            con.close();
            sess.close();
            System.out.println("Connection closed");

        } catch (JMSException e) {
            e.printStackTrace();
            throw new PublishingFailedException();
        }
    }
}
