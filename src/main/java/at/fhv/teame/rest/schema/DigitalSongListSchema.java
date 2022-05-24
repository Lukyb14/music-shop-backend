package at.fhv.teame.rest.schema;

import at.fhv.teame.sharedlib.dto.DigitalSongDTO;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class DigitalSongListSchema {

    public DigitalSongListSchema(List<DigitalSongDTO> digitalSongDTOs) {
        this.digitalSongDTOs = digitalSongDTOs;
    }

    @XmlElement
    public List<DigitalSongDTO> digitalSongDTOs;

}
