package at.fhv.teame.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class ShoppingCartSchema {
    @XmlElement
    public Map<String, Integer> cartSongs;
    @XmlElement
    public String email;
    @XmlElement
    public String cvc;
    @XmlElement
    public String token;
}
