package at.fhv.teame.domain;

import javax.persistence.*;
import java.math.BigDecimal;

public class InvoiceLine {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @ManyToOne
    private Invoice invoice;
    // Soundcarrier manytoone oder one-to-one?
    @OneToOne
    private SoundCarrier soundCarrier;
    @Column
    private int quantity;
    // nehmen oder nicht, weil preis kann sich ändern?
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