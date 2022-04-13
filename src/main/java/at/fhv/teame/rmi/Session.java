package at.fhv.teame.rmi;

import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.domain.model.user.Role;

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
        if (Instant.now().isAfter(expiryDate)) {
            return false;
        }

        switch (clientUser.getRole()) {
            case SELLER:
            case ADMINISTRATOR:
                return true;
            default:
                return false;
        }
    }

    public boolean isAdmin() {
        if (Instant.now().isAfter(expiryDate)) {
            return false;
        }

        return clientUser.getRole().equals(Role.ADMINISTRATOR);
    }

    public boolean isOperator() {
        if (Instant.now().isAfter(expiryDate)) {
            return false;
        }

        switch (clientUser.getRole()) {
            case OPERATOR:
            case ADMINISTRATOR:
                return true;
            default:
                return false;
        }
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