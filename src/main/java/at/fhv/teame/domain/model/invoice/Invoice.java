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
    @Column
    private String customerFirstName;
    @Column
    private String customerLastName;
    @Column
    private String customerAddress;
    @Column
    private LocalDate dateOfPurchase;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Column
    private BigDecimal totalPrice;
    @OneToMany(mappedBy = "invoice")
    private List<InvoiceLine> purchasedItems;
    @Column
    private BigDecimal toRefund;

    // required by hibernate
    protected Invoice() {
    }

    public Invoice(LocalDate dateOfPurchase, PaymentMethod paymentMethod) {
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
        this.totalPrice = new BigDecimal(0);
        this.purchasedItems = new ArrayList<>();
        this.toRefund = new BigDecimal(0);
    }

    public Invoice(LocalDate dateOfPurchase, PaymentMethod paymentMethod, String customerFirstName, String customerLastName, String customerAddress) {
        this.customerFirstName = customerFirstName;
        this.customerLastName = customerLastName;
        this.customerAddress = customerAddress;
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
        this.totalPrice = new BigDecimal(0);
        this.purchasedItems = new ArrayList<>();
        this.toRefund = new BigDecimal(0);

    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
        this.toRefund = toRefund.add(totalPrice);
    }

    public void setPurchasedItems(List<InvoiceLine> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }

    public Long getInvoiceId() {
        return invoiceId;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public String getCustomerAddress() {
        return customerAddress;
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

    public BigDecimal getToRefund() {
        return toRefund;
    }

}
