package at.fhv.teame.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Map;

@XmlRootElement
public class ShoppingCartSchema {
    @XmlElement
    public Map<String, Integer> purchasedItems;
    @XmlElement
    public String paymentMethod;
    @XmlElement
    public String customerFirstName;
    @XmlElement
    public String customerLastName;
    @XmlElement
    public String customerAddress;
    @XmlElement
    public String token;
}
