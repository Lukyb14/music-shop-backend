package at.fhv.teame.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
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

    public Long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }

    public Medium getMedium() {
        return medium;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
