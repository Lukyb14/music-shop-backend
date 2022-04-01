package at.fhv.teame.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Invoice {
    @Id
    @Column
    @GeneratedValue
    private Long id;
    @GeneratedValue(generator = "seq_invoiceId", strategy = GenerationType.SEQUENCE)
    @Column(unique = true)
    private String invoiceId;
   /*
    TODO: Customer class, get them from customer db
    @Column
    private String customerFirstName;
    @Column
    private String customerLastName;
    @Column
    private String customerAddress;
    */
    @Column
    private LocalDate dateOfPurchase;
    @Column
    private PaymentMethod paymentMethod;
    @Column
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "invoiceLine")
    private List<InvoiceLine> purchasedItems;

    // required by hibernate
    protected Invoice() {}

    public Invoice(String invoiceId, LocalDate dateOfPurchase, PaymentMethod paymentMethod,
                   BigDecimal totalPrice, List<InvoiceLine> purchasedItems) {
        this.invoiceId = invoiceId;
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.purchasedItems = purchasedItems;
    }

    public Long getId() {
        return id;
    }

    public String getInvoiceId() {
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
