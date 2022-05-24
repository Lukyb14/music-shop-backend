package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class DigitalInvoiceLine {

    @Id
    @Column
    @GeneratedValue
    private Long id;
    @ManyToOne
    private DigitalInvoice digitalInvoice;
    @OneToOne
    private DigitalSong product;
    @Column
    private BigDecimal price;

    // required by hibernate
    protected DigitalInvoiceLine() { }

    public DigitalInvoiceLine(DigitalInvoice digitalInvoice, DigitalSong product, BigDecimal price) {
        this.digitalInvoice = digitalInvoice;
        this.product = product;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public DigitalInvoice getDigitalInvoice() {
        return digitalInvoice;
    }

    public DigitalSong getProduct() {
        return product;
    }


    public BigDecimal getPrice() {
        return price;
    }
}
