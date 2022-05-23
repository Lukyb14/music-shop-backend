package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.*;
import java.util.List;

@Entity
public class PurchaseHistory {
    @Id
    private Long id;

    @OneToMany(mappedBy = "digitalsong")
    private List<DigitalSong> purchasedSongs;

    protected PurchaseHistory() {

    }





    public String getEmail() {
        return email;
    }

    public List<DigitalSong> getPurchasedSongs() {
        return purchasedSongs;
    }
}

