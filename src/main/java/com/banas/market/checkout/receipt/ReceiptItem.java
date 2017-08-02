package com.banas.market.checkout.receipt;

import com.banas.market.checkout.inventory.Item;

import javax.persistence.*;

@Entity
@Table(name = "RECEIPT_ITEM")
public class ReceiptItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @OneToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "RECEIPT_ID", nullable = false)
    private Receipt receipt;

    ReceiptItem(Receipt receipt, Item item) {
        this.receipt = receipt;
        this.item = item;
        this.quantity = 0;
    }

    void increaseQuantity() {
        quantity++;
    }

    Item getItem() {
        return item;
    }

    public Integer getQuantity() {
        return new Integer(quantity);
    }

    @Override
    public String toString() {
        return item + ", quantity=" + quantity;
    }
}
