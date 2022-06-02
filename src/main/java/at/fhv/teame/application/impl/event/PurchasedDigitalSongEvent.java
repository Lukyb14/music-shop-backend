package at.fhv.teame.application.impl.event;

import at.fhv.teame.application.impl.event.dto.DigitalSongDTO;

import java.io.Serializable;
import java.util.List;

public class PurchasedDigitalSongEvent implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;

    private String userId;

    private List<DigitalSongDTO> digitalSongDTOList;

    public PurchasedDigitalSongEvent(String userId, List<DigitalSongDTO> digitalSongDTOList) {
        this.userId = userId;
        this.digitalSongDTOList = digitalSongDTOList;
    }

    public String getUserId() {
        return userId;
    }

    public List<DigitalSongDTO> getDigitalSongDTOList() {
        return digitalSongDTOList;
    }
}
