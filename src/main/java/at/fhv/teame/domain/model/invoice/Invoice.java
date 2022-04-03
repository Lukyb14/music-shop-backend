package at.fhv.teame.domain.model.invoice;

import at.fhv.teame.domain.model.PaymentMethod;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(generator = "seq_gen", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "seq_gen", sequenceName = "seq_invoiceid", allocationSize = 1, initialValue = 20000)
    private Long invoiceId;
    // TODO: Value objects and packages

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
    @OneToMany (mappedBy = "invoice")
    private List<InvoiceLine> purchasedItems;

    // required by hibernate
    protected Invoice() {}

    public Invoice(LocalDate dateOfPurchase, PaymentMethod paymentMethod,
                   BigDecimal totalPrice) {
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.purchasedItems = new ArrayList<>();
    }

    public void addInvoiceItem(InvoiceLine invoiceLine) {
        purchasedItems.add(invoiceLine);
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
}
