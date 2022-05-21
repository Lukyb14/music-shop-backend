package at.fhv.teame.application.impl;

import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.middleware.AuthenticationServiceImpl;
import at.fhv.teame.middleware.MessagingServiceImpl;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.ejb.MessageServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import at.fhv.teame.sharedlib.exceptions.ReceiveFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessagingServiceTest {
    private MessageServiceRemote messageService;
    private AuthenticationServiceRemote authenticationService;

    @BeforeEach
    void setup() {
        messageService = new MessagingServiceImpl(new HibernateUserRepository(), new ListSessionRepository());
        authenticationService = new AuthenticationServiceImpl(new HibernateUserRepository(), new ListSessionRepository());

    }

    @Test
    void getAllTopics() {
        //given
        List<String> topicsExpected = List.of(
                "Pop",
                "Rock",
                "System.Message"
        );

        //when
        List<String> topicsActual = messageService.allTopics();

        //then
        assertEquals(topicsExpected, topicsActual);
    }

    @Test
    void fetchMessages() throws InvalidSessionException, ReceiveFailedException, LoginFailedException {
        SessionDTO sessionDTO = authenticationService.login("har9090", "PssWrd");
        List<MessageDTO> receiveMessageDTOS = messageService.fetchMessages(sessionDTO.getSessionId());
        assertTrue(receiveMessageDTOS.size() > 0);
    }
}
