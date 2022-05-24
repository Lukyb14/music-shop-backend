package at.fhv.teame.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class ShoppingCartDigitalSongSchema {
    @XmlElement
    public Map<String, Integer> cartSongIds;
    @XmlElement
    public String email;
    @XmlElement
    public String cvc;
    @XmlElement
    public String token;
}
