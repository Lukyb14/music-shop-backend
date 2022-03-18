package at.fhv.teame.domain;

import java.math.BigDecimal;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class SoundCarrier {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @OneToOne
    private Album album;
    @Enumerated(EnumType.STRING)
    private Medium medium;
    @Column
    private BigDecimal price;
    @Column
    private int stock;

    //required for Hibernate
    protected SoundCarrier() {}

    public SoundCarrier(Album album, Medium medium, BigDecimal price, int stock) {
        this.album = album;
        this.medium = medium;
        this.price = price;
        this.stock = stock;
    }

}
