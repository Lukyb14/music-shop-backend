package at.fhv.teame.application.impl;

import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.rmi.MessageService;
import at.fhv.teame.sharedlib.rmi.exceptions.PublishingFailedException;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
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
            //Create new ConnectionFactory
            ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://10.0.40.169:61616");
            // Create a connection
            Connection con = cf.createConnection();
            // create a JMS session
            Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination dest = sess.createTopic(publishMessageDTO.getTopic());
            // Create some Message and a MessageProducer with the session
            TextMessage msg = sess.createTextMessage(publishMessageDTO.getContent());
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

    @Override
    public List<String> allTopics() {
        return userRepository.allTopics();
    }


    public void receiveMessage() throws PublishingFailedException {
        try {
            //Create new ConnectionFactory
            ConnectionFactory cf = new ActiveMQConnectionFactory("tcp://10.0.40.169:61616");
            Connection con = cf.createConnection();
            con.setClientID("DurableSubscriber");
            Session sess = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // Start the connection: msg delivery can begin with existing consumer
            con.start();

            Topic dest = sess.createTopic("Rock");
            // Create a MessageConsumer
            MessageConsumer consumer = sess.createDurableSubscriber(dest, "Listener");
            // Receive the messages sent to the destination
            TextMessage msg = (TextMessage) consumer.receiveNoWait();
            if (msg != null) System.out.println(msg.getText());
            // Close the connection
            con.close();
            System.out.println("Connection closed");
        } catch (JMSException e) {
            throw new PublishingFailedException();
        }
    }




}
