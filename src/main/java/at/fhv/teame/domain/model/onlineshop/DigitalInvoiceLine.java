package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class DigitalInvoiceLine {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @OneToOne
    private DigitalSong product;
    @Column
    private BigDecimal price;

    // required by hibernate
    protected DigitalInvoiceLine() { }

    public DigitalInvoiceLine(DigitalSong product, BigDecimal price) {
        this.product = product;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public DigitalSong getProduct() {
        return product;
    }


    public BigDecimal getPrice() {
        return price;
    }
}
