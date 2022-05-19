package at.fhv.teame.application.rest.schema;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginSchema {
    @XmlElement
    public String username;
    @XmlElement
    public String password;
}
