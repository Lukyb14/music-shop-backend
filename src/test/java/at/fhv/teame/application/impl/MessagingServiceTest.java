package at.fhv.teame.application.impl;

import at.fhv.teame.infrastructure.HibernateUserRepository;
import at.fhv.teame.infrastructure.ListSessionRepository;
import at.fhv.teame.middleware.MessagingServiceImpl;
import at.fhv.teame.sharedlib.dto.MessageDTO;
import at.fhv.teame.sharedlib.ejb.MessageServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.ReceiveFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessagingServiceTest {
    private MessageServiceRemote messageService;

    @BeforeEach
    void setup() {
        messageService = new MessagingServiceImpl(new HibernateUserRepository(), new ListSessionRepository());
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
    void fetchMessages() throws InvalidSessionException, ReceiveFailedException {
        List<MessageDTO> receiveMessageDTOS = messageService.fetchMessages("b16c5200-bb0e-11ec-8422-0242ac120003");
        assertTrue(receiveMessageDTOS.size() > 0);
    }
}
