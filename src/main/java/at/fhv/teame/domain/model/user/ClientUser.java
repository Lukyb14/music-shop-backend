package at.fhv.teame.domain.model.user;

import javax.persistence.*;

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

    // for hibernate
    protected ClientUser() {
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
}
