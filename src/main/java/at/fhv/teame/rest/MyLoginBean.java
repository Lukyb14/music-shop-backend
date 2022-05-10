package at.fhv.teame.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MyLoginBean {
    @XmlElement
    public String username;
    @XmlElement
    public String password;
}
