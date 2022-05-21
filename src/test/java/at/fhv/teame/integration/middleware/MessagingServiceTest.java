package at.fhv.teame.integration.middleware;

import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.middleware.AuthenticationServiceImpl;
import at.fhv.teame.middleware.MessagingServiceImpl;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.ejb.MessageServiceRemote;
import at.fhv.teame.sharedlib.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MessagingServiceTest {
    private MessageServiceRemote messageService;
    private AuthenticationServiceRemote authenticationService;

    @BeforeEach
    void setup() {
        messageService = new MessagingServiceImpl(new HibernateUserRepository(), new ListSessionRepository());
        authenticationService = new AuthenticationServiceImpl(new HibernateUserRepository(), new ListSessionRepository());
    }

    @Test
    void allTopics(){
        List<String> expectedTopics = List.of("Pop", "Rock", "System.Message");

        List<String> actualTopics = messageService.allTopics();

        assertEquals(expectedTopics, actualTopics);
    }

    @Test
    void publishMessage() throws LoginFailedException, InvalidSessionException, ReceiveFailedException, PublishingFailedException, InterruptedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        String randomValue = UUID.randomUUID().toString();
        messageService.publishMessage(new MessageDTO("Rock", "New Concert", randomValue));

        // wait 1 second for ActiveMQ to update itself
        Thread.sleep(10000);

        List<MessageDTO> messagesAfter = messageService.fetchMessages(sessionDTO.getSessionId());
        boolean isMessagePresent = false;
        for (MessageDTO message : messagesAfter) {
            if (message.getContent().equals(randomValue)) {
                isMessagePresent = true;
                break;
            }
        }

        assertTrue(isMessagePresent);
    }

    @Test
    void deleteMessage() throws LoginFailedException, InvalidSessionException, ReceiveFailedException, DeletionFailedException, InterruptedException {
        SessionDTO sessionDTO = authenticationService.login("lbo3144", "PssWrd");

        List<MessageDTO> messagesBeforeDeletion = messageService.fetchMessages(sessionDTO.getSessionId());
        String deletedMessageId = messagesBeforeDeletion.get(0).getId();
        String deletedMessageTopic = messagesBeforeDeletion.get(0).getTopic();

        messageService.deleteMessage(deletedMessageTopic, deletedMessageId, sessionDTO.getSessionId());

        // wait 4 seconds for ActiveMQ to update itself
        Thread.sleep(6000);

        List<MessageDTO> messagesAfterDeletion = messageService.fetchMessages(sessionDTO.getSessionId());
        boolean isMessagePresent = false;
        for (MessageDTO message : messagesAfterDeletion) {
            if (message.getId().equals(deletedMessageId)) {
                isMessagePresent = true;
                break;
            }
        }
        assertFalse(isMessagePresent);
    }


    @Test
    void fetchMessages() throws LoginFailedException, InvalidSessionException, ReceiveFailedException {

        SessionDTO sessionDTO = authenticationService.login("aci9089", "PssWrd");

        List<MessageDTO> messageDTOs = messageService.fetchMessages(sessionDTO.getSessionId());

        assertTrue(messageDTOs.size() > 0);

        for (MessageDTO dto : messageDTOs) {
            assertEquals("System.Message", dto.getTopic());
        }
    }
}
