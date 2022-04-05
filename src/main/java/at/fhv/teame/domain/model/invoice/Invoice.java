package at.fhv.teame.domain.model.invoice;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_invoiceid", allocationSize = 1, initialValue = 20000)
    private Long invoiceId;

    /*
    TODO: get them from customer db
    @Column
    private String customerFirstName;
    @Column
    private String customerLastName;
    @Column
    private String customerAddress;
    */

    @Column
    private LocalDate dateOfPurchase;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceLine> purchasedItems;
    @Column
    private BigDecimal currentRefundable;
    @Column
    private BigDecimal totalRefundable;

    // required by hibernate
    protected Invoice() {
    }

    public Invoice(LocalDate dateOfPurchase, PaymentMethod paymentMethod) {
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
        this.totalPrice = new BigDecimal(0);
        this.purchasedItems = new ArrayList<>();
        this.currentRefundable = new BigDecimal(0);
        this.totalRefundable = new BigDecimal(0);
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        this.currentRefundable = currentRefundable.add(totalPrice);
        this.totalRefundable = totalRefundable.add(totalPrice);
    }

    public void setPurchasedItems(List<InvoiceLine> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public List<InvoiceLine> getPurchasedItems() {
        return purchasedItems;
    }

    public BigDecimal getCurrentRefundable() {
        return currentRefundable;
    }

    public BigDecimal getTotalRefundable() {
        return totalRefundable;
    }
}
