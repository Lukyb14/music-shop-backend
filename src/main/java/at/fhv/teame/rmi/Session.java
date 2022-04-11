package at.fhv.teame.rmi;

import at.fhv.teame.domain.model.user.ClientUser;

import java.time.Instant;
import java.util.UUID;

public class Session {

    private static final int DURATION_OF_VALIDITY = 3600;

    private UUID sessionId;
    private ClientUser clientUser;
    private Instant expiryDate;

    public Session(ClientUser clientUser) {
        this.sessionId = UUID.randomUUID();
        this.clientUser = clientUser;
        this.expiryDate = Instant.now().plusSeconds(DURATION_OF_VALIDITY);
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public ClientUser getUser() {
        return clientUser;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }
}
