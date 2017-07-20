package com.banas.market.checkout.receipt.entities;

import com.banas.market.checkout.inventory.Item;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "RECEIPT_ITEM_HISTORY")
public class ReceiptItemHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "RECEIPT_HISTORY_ID", nullable = false)
    private ReceiptHistory receiptHistory;

    public ReceiptItemHistory(Item item, Integer quantity, ReceiptHistory receiptHistory) {
        this.unitPrice = item.getPrice();
        this.quantity = quantity;
        this.name = item.getName();
        this.receiptHistory = receiptHistory;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return unitPrice;
    }

    public String getName() {
        return name;
    }

    public ReceiptHistory getReceiptHistory() {
        return receiptHistory;
    }
}
