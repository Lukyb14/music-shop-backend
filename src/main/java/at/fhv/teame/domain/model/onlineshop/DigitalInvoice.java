package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
public class DigitalInvoice {

    @Id
    @GeneratedValue(generator = "seq_gen2", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gen2", sequenceName = "seq_digital_invoiceid", allocationSize = 1, initialValue = 20000)
    private Long invoiceId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private BigDecimal totalPrice;

    @Column
    private LocalDateTime date;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="invoiceId", nullable=false)
    private List<DigitalInvoiceLine> purchasedItems;

    // required by hibernate
    protected DigitalInvoice() { }

    public DigitalInvoice(String firstName, String lastName, String email, List<DigitalInvoiceLine> purchasedItems) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.date = LocalDateTime.now();
        this.purchasedItems = purchasedItems;
        calcTotalPrice();
    }

    private void calcTotalPrice() {
        this.totalPrice = BigDecimal.valueOf(0);
        for (DigitalInvoiceLine d : purchasedItems) {
            this.totalPrice = this.totalPrice.add(d.getPrice());
        }
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<DigitalInvoiceLine> getPurchasedItems() {
        return purchasedItems;
    }
}
