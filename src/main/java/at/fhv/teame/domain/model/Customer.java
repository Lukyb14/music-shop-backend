package at.fhv.teame.domain.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Customer {
    @Id
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;

    @ElementCollection
    @CollectionTable(name = "customer_purchase_history", joinColumns = @JoinColumn(name = "id"))
    @Column
    private List<Long> purchasedSongs;


    protected Customer() { }


    public String getEmail() {
        return email;
    }

    public List<Long> getPurchasedSongIds() {
        return purchasedSongs;
    }
}

