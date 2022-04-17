package at.fhv.teame.application.impl;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.sharedlib.dto.PublishMessageDTO;
import at.fhv.teame.sharedlib.dto.ReceiveMessageDTO;
import at.fhv.teame.sharedlib.rmi.MessageService;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.rmi.exceptions.PublishingFailedException;
import at.fhv.teame.sharedlib.rmi.exceptions.ReceiveFailedException;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public class MessagingServiceImpl extends UnicastRemoteObject implements MessageService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    //default constructor with hibernate
    public MessagingServiceImpl() throws RemoteException {
        this(new HibernateUserRepository(), new ListSessionRepository());
    }

    //for mocking
    public MessagingServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) throws RemoteException {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void publishMessage(PublishMessageDTO publishMessageDTO, String sessionId) throws RemoteException, PublishingFailedException, InvalidSessionException {
        try {
            at.fhv.teame.rmi.Session rmiSession = sessionRepository.sessionById(UUID.fromString(sessionId));
            if (!rmiSession.isOperator()) throw new InvalidSessionException();

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
            Message msg = sess.createTextMessage(publishMessageDTO.getTitle() + "/*/*" + publishMessageDTO.getContent());
            MessageProducer prod = sess.createProducer(dest);
            // send the message to the destination
            prod.send(msg);
            // Close the connection
            con.close();
            sess.close();
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            throw new PublishingFailedException();
        } catch (SessionNotFoundException e) {
            throw new InvalidSessionException();
        }
    }

    @Override
    public List<String> allTopics(String sessionId) throws InvalidSessionException {
        try {
            sessionRepository.sessionById(UUID.fromString(sessionId));
            return userRepository.allTopics();
        } catch (SessionNotFoundException e) {
            throw new InvalidSessionException();
        }
    }

    @Override
    public List<ReceiveMessageDTO> fetchMessages(String sessionId) throws RemoteException, ReceiveFailedException, InvalidSessionException {
        try {
            at.fhv.teame.rmi.Session rmiSession = sessionRepository.sessionById(UUID.fromString(sessionId));
            ClientUser clientUser = rmiSession.getUser();

            List<ReceiveMessageDTO> messages = new LinkedList<>();

            for(String topic : clientUser.getTopics()) {
                messages.addAll(receiveAllTopicMessages(clientUser, topic));
            }

            messages.sort(Comparator.comparing(ReceiveMessageDTO::getTimestamp).reversed());
            return messages;

        } catch (SessionNotFoundException e){
            throw new InvalidSessionException();
        }
    }

    private List<ReceiveMessageDTO> receiveAllTopicMessages(ClientUser clientUser, String topic) throws ReceiveFailedException {
        try {
            InitialContext ctx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            Topic dest = (Topic) ctx.lookup(topic);
            Connection con = cf.createConnection();
            con.setClientID(clientUser.getCn());
            Session sess = con.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            // Start the connection: msg delivery can begin with existing consumer
            con.start();
            // Create a MessageConsumer
            MessageConsumer consumer = sess.createDurableSubscriber(dest, topic);
            // Receive the messages sent to the destination
            List<ReceiveMessageDTO> messageDTOs = new LinkedList<>();
            TextMessage msg;
            do {
                msg = (TextMessage) consumer.receive(1);
                if (msg != null) {
                    messageDTOs.add(new ReceiveMessageDTO(topic, splitSubjectFromMessage(msg.getText()), splitContentFromMessage(msg.getText()), msg.getJMSTimestamp()));
                }
            } while (msg != null);

            // Close the connection
            con.close();
            return messageDTOs;
        } catch (JMSException | NamingException e) {
            throw new ReceiveFailedException();
        }
    }

    // TODO fix subject / content splitting

    private String splitContentFromMessage(String message) {
        try {
            return message.split("\\*\\/\\*\\/")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return message;
        }
    }

    private String splitSubjectFromMessage(String message) {
        return message.split("\\*\\/\\*\\/")[0];
    }
}
