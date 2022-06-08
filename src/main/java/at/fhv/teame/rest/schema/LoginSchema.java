package at.fhv.teame.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginSchema {
    @XmlElement
    public String userId;
    @XmlElement
    public String password;
}
