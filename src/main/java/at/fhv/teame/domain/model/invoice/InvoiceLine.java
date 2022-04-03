package at.fhv.teame.domain.model.invoice;

import at.fhv.teame.domain.model.SoundCarrier;

import javax.persistence.*;
import java.math.BigDecimal;

public class InvoiceLine {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Invoice invoice;
    @OneToOne
    private SoundCarrier soundCarrier;
    @Column
    private int quantity;
    @Column
    private BigDecimal price;

    // required by hibernate
    protected InvoiceLine() {}

    public InvoiceLine(Invoice invoice, SoundCarrier soundCarrier, int quantity, BigDecimal price) {
        this.invoice = invoice;
        this.soundCarrier = soundCarrier;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public SoundCarrier getSoundCarrier() {
        return soundCarrier;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
}