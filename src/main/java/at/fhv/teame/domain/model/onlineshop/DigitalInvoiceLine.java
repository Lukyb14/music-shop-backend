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
    private int quantity;
    @Column
    private BigDecimal price;

    // required by hibernate
    protected DigitalInvoiceLine() { }

    public DigitalInvoiceLine(DigitalSong product, int quantity, BigDecimal price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }


    public DigitalSong getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
