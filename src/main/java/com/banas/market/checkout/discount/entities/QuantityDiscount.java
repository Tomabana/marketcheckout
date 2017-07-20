package com.banas.market.checkout.discount.entities;

import com.banas.market.checkout.inventory.Item;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "QUANTITY_DISCOUNT")
public class QuantityDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Quantity discount {" +
                "item=" + item.getName() +
                ", " + quantity +
                " for " + price +
                '}';
    }
}
