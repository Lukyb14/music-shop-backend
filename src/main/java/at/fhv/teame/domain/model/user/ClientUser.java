package at.fhv.teame.domain.model.user;

import at.fhv.teame.domain.model.invoice.Invoice;
import at.fhv.teame.domain.model.invoice.InvoiceLine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ClientUser {
    @Id
    private String cn;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ElementCollection
    @CollectionTable(name="clientuser_topics", joinColumns=@JoinColumn(name="cn"))
    @Column
    private List<String> topics;

    // for hibernate
    protected ClientUser() {
    }

    public ClientUser(String cn, String firstName, String lastName, Role role) {
        this.cn = cn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        topics = new ArrayList<>();
    }

    public String getCn() {
        return cn;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public List<String> getTopics() {
        return topics;
    }
}
