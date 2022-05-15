package at.fhv.teame.application.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LoginSchema {
    @XmlElement
    public String username;
    @XmlElement
    public String password;
}
