package com.banas.market.checkout.discount;

import com.banas.market.checkout.inventory.Item;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

@Entity
@Table(name = "QUANTITY_DISCOUNT")
public class QuantityDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "PRICE")
    private BigDecimal price;

    public BigDecimal getDiscount() {
        BigDecimal priceWithoutDiscount = item.getPrice().multiply(BigDecimal.valueOf(quantity));
        return priceWithoutDiscount.subtract(price);
    }

    public Integer getQuantity() {
        return new Integer(quantity);
    }

    boolean checkIfDiscountCanApply(Map<Item, Integer> itemsInBasket) {
        return itemsInBasket.get(item) != null && itemsInBasket.get(item) >= quantity;
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
