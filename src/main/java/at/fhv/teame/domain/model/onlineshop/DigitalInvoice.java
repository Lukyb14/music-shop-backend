package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class DigitalInvoice {

    @Id
    @GeneratedValue(generator = "seq_gen2", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gen2", sequenceName = "seq_digital_invoiceid", allocationSize = 1, initialValue = 20000)
    private Long invoiceId;

    @Column
    private String email;

    @Column
    private BigDecimal totalPrice;

    @Column
    private LocalDateTime date;

    @OneToMany(mappedBy = "digitalInvoice")
    private List<DigitalInvoiceLine> purchasedItems;

    // required by hibernate
    protected DigitalInvoice() {
    }

    public DigitalInvoice(String email) {
        this.email = email;
        this.date = LocalDateTime.now();
        this.purchasedItems = new ArrayList<>();
        calcTotalPrice();
    }

    private void calcTotalPrice() {
        this.totalPrice = BigDecimal.valueOf(0);
        for (DigitalInvoiceLine d : purchasedItems) {
            this.totalPrice = this.totalPrice.add(d.getPrice());
        }
    }

    public void setPurchasedItems(List<DigitalInvoiceLine> purchasedItems) {
        this.purchasedItems = purchasedItems;
        calcTotalPrice();
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
