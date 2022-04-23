package at.fhv.teame.integration;

import at.fhv.teame.application.impl.MessagingServiceImpl;
import at.fhv.teame.sharedlib.rmi.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessagingTest {
    private MessageService messageService;

    @BeforeEach
    void setup() throws RemoteException {
        messageService = new MessagingServiceImpl();
    }

    @Test
    void getAllTopics() throws RemoteException {
        //given
        List<String> topicsExpected = List.of(
                "Pop",
                "Rock",
                "System.Messages"
        );

        //when
        List<String> topicsActual = messageService.allTopics();

        //then
        assertEquals(topicsExpected, topicsActual);
    }
}
