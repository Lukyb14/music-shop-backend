package at.fhv.teame.domain.model;

import at.fhv.teame.domain.exceptions.InvalidAmountException;
import at.fhv.teame.domain.exceptions.OutOfStockException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class SoundCarrier {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String articleId;
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

    public SoundCarrier(String articleId, Album album, Medium medium, BigDecimal price, int stock) {
        this.articleId = articleId;
        this.album = album;
        this.medium = medium;
        this.price = price;
        this.stock = stock;
    }

    public void retrieve(int amount) throws OutOfStockException, InvalidAmountException {
        if (stock - amount < 0) {
            throw new OutOfStockException();
        }
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        stock -= amount;
    }

    public String getAlbumName() {
        return this.album.getName();
    }

    public String getAlbumLabel() {
        return this.album.getLabel();
    }

    public String getAlbumGenre() {
        return this.album.getGenre();
    }

    public String getAlbumArtist() {
        return this.album.getArtist();
    }

    public List<Song> getAlbumSongs() {
        return this.album.getSongs();
    }

    public String getArticleId() {
        return articleId;
    }

    public Long getId() {
        return id;
    }

    public Album getAlbum() {
        return album;
    }

    public String getMedium() {
        return medium.toString();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }
}
