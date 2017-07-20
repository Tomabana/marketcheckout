package com.banas.market.checkout.receipt.entities;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RECEIPT_DISCOUNT_HISTORY")
public class ReceiptDiscountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TOTAL_DISCOUNT")
    private BigDecimal totalDiscount;

    @ManyToOne
    @JoinColumn(name = "RECEIPT_HISTORY_ID", nullable = false)
    private ReceiptHistory receiptHistory;

    public ReceiptDiscountHistory(BigDecimal totalDiscount, ReceiptHistory receiptHistory) {
        this.totalDiscount = totalDiscount;
        this.receiptHistory = receiptHistory;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }


    public ReceiptHistory getReceiptHistory() {
        return receiptHistory;
    }

}
