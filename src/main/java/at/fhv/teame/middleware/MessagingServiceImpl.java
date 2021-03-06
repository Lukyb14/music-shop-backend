package at.fhv.teame.middleware;

import at.fhv.teame.application.exceptions.SessionNotFoundException;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.repositories.SessionRepository;
import at.fhv.teame.domain.repositories.UserRepository;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.ejb.MessageServiceRemote;
import at.fhv.teame.sharedlib.exceptions.DeletionFailedException;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.PublishingFailedException;
import at.fhv.teame.sharedlib.exceptions.ReceiveFailedException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Stateless
public class MessagingServiceImpl implements MessageServiceRemote {
    @EJB
    private UserRepository userRepository;

    @EJB
    private SessionRepository sessionRepository;

    public MessagingServiceImpl() { }

    //for mocking
    public MessagingServiceImpl(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }


    @Override
    public void publishMessage(MessageDTO messageDTO) throws PublishingFailedException {
        try {
            // Get the JNDI Initial Context to do JNDI lookups
            InitialContext ctx = new InitialContext();
            // Get the ConnectionFactory by JNDI name
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            // get the Destination used to send the message by JNDI name
            Destination dest = (Destination) ctx.lookup(messageDTO.getTopic());
            // Create a connection
            Connection con = cf.createConnection();
            // create a JMS session
            Session sess = con.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            // Create some Message and a MessageProducer with the session
            Message msg = sess.createTextMessage(messageDTO.getSubject() + "//" + messageDTO.getContent());
            MessageProducer prod = sess.createProducer(dest);
            // send the message to the destination
            prod.send(msg);
            // Close the connection
            con.close();
            sess.close();
        } catch (JMSException | NamingException e) {
            e.printStackTrace();
            throw new PublishingFailedException();
        }
    }

    @Override
    public List<String> allTopics() {
        return userRepository.allTopics();
    }

    @Override
    public void deleteMessage(String topic, String messageId, String sessionId) throws InvalidSessionException, DeletionFailedException {
        try {
            at.fhv.teame.domain.model.session.Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            ClientUser clientUser = session.getUser();
            searchMessageToAcknowledge(clientUser, topic, messageId);

        } catch (DeletionFailedException e) {
            e.printStackTrace();
            throw new DeletionFailedException();
        } catch (SessionNotFoundException e) {
            throw new InvalidSessionException();
        }
    }


    private void searchMessageToAcknowledge(ClientUser clientUser, String topic, String messageId) throws DeletionFailedException {
        try {
            InitialContext ctx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            Topic dest = (Topic) ctx.lookup(topic);
            Connection con = cf.createConnection();
            con.setClientID(clientUser.getCn());
            Session sess = con.createSession(false, 4);     //4 -> IndividualAcknowledge
            con.start();
            MessageConsumer consumer = sess.createDurableSubscriber(dest, topic);
            TextMessage msg;
            do {
                msg = (TextMessage) consumer.receive(1);
                if (msg != null && msg.getJMSMessageID().equals(messageId)) {
                    msg.acknowledge();
                    break;
                }
            } while (msg != null);

            // Close the connection
            con.close();
        } catch (JMSException | NamingException e) {
            throw new DeletionFailedException();
        }
    }

    @Override
    public List<MessageDTO> fetchMessages(String sessionId) throws ReceiveFailedException, InvalidSessionException {
        try {
            at.fhv.teame.domain.model.session.Session session = sessionRepository.sessionById(UUID.fromString(sessionId));
            ClientUser clientUser = session.getUser();

            List<MessageDTO> messages = new LinkedList<>();
            for (String topic : clientUser.getTopics()) {
                messages.addAll(receiveAllTopicMessages(clientUser, topic));
            }

            messages.sort(Comparator.comparing(MessageDTO::getTimestamp).reversed());
            return messages;
        } catch (SessionNotFoundException e) {
            throw new InvalidSessionException();
        }
    }

    private List<MessageDTO> receiveAllTopicMessages(ClientUser clientUser, String topic) throws ReceiveFailedException {
        try {
            InitialContext ctx = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) ctx.lookup("connectionFactory");
            Topic dest = (Topic) ctx.lookup(topic);
            Connection con = cf.createConnection();
            con.setClientID(clientUser.getCn());
            Session sess = con.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            con.start();
            MessageConsumer consumer = sess.createDurableSubscriber(dest, topic);
            List<MessageDTO> messageDTOs = new LinkedList<>();
            TextMessage msg;
            do {
                msg = (TextMessage) consumer.receive(1);
                if (msg != null) {
                    messageDTOs.add(new MessageDTO(msg.getJMSMessageID(), topic, splitSubjectFromMessage(msg.getText()), splitContentFromMessage(msg.getText()), msg.getJMSTimestamp()));
                }
            } while (msg != null);

            // Close the connection
            con.close();
            return messageDTOs;
        } catch (JMSException | NamingException e) {
            throw new ReceiveFailedException();
        }
    }

    private String splitContentFromMessage(String message) {
        try {
            return message.split("//")[1];
        } catch (ArrayIndexOutOfBoundsException e) {
            return message;
        }
    }

    private String splitSubjectFromMessage(String message) {
        return message.split("//")[0];
    }
}
