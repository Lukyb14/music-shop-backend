package at.fhv.teame.application.impl;

import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.rmi.MessageService;
import at.fhv.teame.sharedlib.rmi.exceptions.PublishingFailedException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MessagingServiceImpl extends UnicastRemoteObject implements MessageService {
    private final UserRepository userRepository;

    //default constructor with hibernate
    public MessagingServiceImpl() throws RemoteException {
        this(new HibernateUserRepository());
    }

    //for mocking
    public MessagingServiceImpl(UserRepository userRepository) throws RemoteException {
        this.userRepository = userRepository;
    }

    @Override
    public void publishMessage(PublishMessageDTO publishMessageDTO, String sessionId) throws RemoteException, PublishingFailedException {
        try {
            // Get the JNDI Initial Context to do JNDI lookups
            InitialContext ctx = new InitialContext();
            // Get the ConnectionFactory by JNDI name
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
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
            sess.close();
            System.out.println("Connection closed");
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            throw new PublishingFailedException();
        }
    }

    @Override
    public List<String> allTopics() {
        return userRepository.allTopics();
    }


    public void receiveMessage() throws PublishingFailedException {
        try {
            InitialContext ctx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            Topic dest = (Topic) ctx.lookup("System.Message");
            Connection con = cf.createConnection();
            con.setClientID("DurableSubscriber");
            Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Start the connection: msg delivery can begin with existing consumer
            con.start();
            // Create a MessageConsumer
            MessageConsumer consumer = sess.createDurableSubscriber(dest, "har9090");
            // Receive the messages sent to the destination
            TextMessage msg = (TextMessage) consumer.receive(1);
            if (msg != null) System.out.println(msg.getText());
            // Close the connection
            con.close();
            System.out.println("Connection closed");
        } catch (JMSException | NamingException e) {
            throw new PublishingFailedException();
        }
    }


}
