package at.fhv.teame.application.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenSchema {
    @XmlElement
    public String token;
}
