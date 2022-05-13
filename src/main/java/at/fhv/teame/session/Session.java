package at.fhv.teame.session;

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

    public boolean isSeller() {
        if (isExpired()) return false;

        switch (clientUser.getRole()) {
            case SELLER:
            case ADMINISTRATOR:
                return true;
            default:
                return false;
        }
    }

    public boolean isOperator() {
        if (isExpired()) return false;

        switch (clientUser.getRole()) {
            case OPERATOR:
            case ADMINISTRATOR:
                return true;
            default:
                return false;
        }
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiryDate);
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public ClientUser getUser() {
        return clientUser;
    }

}
