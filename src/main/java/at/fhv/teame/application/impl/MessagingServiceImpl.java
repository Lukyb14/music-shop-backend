package at.fhv.teame.application.impl;

import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.rmi.MessageService;

import javax.naming.*;
import javax.jms.*;
import java.rmi.RemoteException;

public class MessagingServiceImpl implements MessageService {
    
    @Override
    public void publishMessage(PublishMessageDTO publishMessageDTO, String sessionId) throws RemoteException, NamingException, JMSException {
        // Get the JNDI Initial Context to do JNDI lookups
        InitialContext ctx = new InitialContext();
        // Get the ConnectionFactory by JNDI name
        ConnectionFactory cf = (ConnectionFactory)ctx.lookup("connectionFactory"); // new ActiveMQConnectionFactory(url);
        // get the Destination used to send the message by JNDI name
        Destination dest = (Destination) ctx.lookup(publishMessageDTO.getTopic());
        // Create a connection
        Connection con = cf.createConnection();
        // create a JMS session
        Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // Create some Message and a MessageProducer with the session
        Message msg = sess.createTextMessage(publishMessageDTO.getContent());
        MessageProducer prod = sess.createProducer(dest);
        // send the message to the destination
        prod.send(msg);
        // Close the connection
        con.close();
        System.out.println("Connection closed");
    }
}
