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
    @ManyToOne
    private SoundCarrier soundCarrier;
    @Column
    private int amount;
    // nehmen oder nicht, weil preis kann sich ändern?
    @Column
    private BigDecimal itemPrice;

    // required by hibernate
    protected InvoiceLine() {}

    public InvoiceLine(Invoice invoice, SoundCarrier soundCarrier, int amount, BigDecimal itemPrice) {
        this.invoice = invoice;
        this.soundCarrier = soundCarrier;
        this.amount = amount;
        this.itemPrice = itemPrice;
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

    public int getAmount() {
        return amount;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }
}