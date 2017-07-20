package com.banas.market.checkout.receipt.entities;

import com.banas.market.checkout.receipt.Receipt;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "RECEIPT_HISTORY")
public class ReceiptHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PRICE")
    private BigDecimal price;

    @Column(name = "TIMESTAMP")
    private Date date;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiptHistory", cascade = CascadeType.ALL)
    private Set<ReceiptItemHistory> receiptItems;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "receiptHistory", cascade = CascadeType.ALL)
    private ReceiptDiscountHistory receiptDiscount;

    public ReceiptHistory(Receipt receipt) {
        this.price = receipt.getTotalPrice();
        this.date = receipt.getTimestamp();
        this.receiptItems =
                receipt.getItems().entrySet().stream().map(entry -> new ReceiptItemHistory(
                        entry.getKey(), entry.getValue(), this)).collect(Collectors.toSet());
        this.receiptDiscount = new ReceiptDiscountHistory(receipt.getTotalDiscount(), this);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public Set<ReceiptItemHistory> getReceiptItems() {
        return receiptItems;
    }

    public ReceiptDiscountHistory getReceiptDiscounts() {
        return receiptDiscount;
    }

}
