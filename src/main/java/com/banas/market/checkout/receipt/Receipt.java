package com.banas.market.checkout.receipt;

import com.banas.market.checkout.discount.model.Discounts;
import com.banas.market.checkout.inventory.Item;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Receipt {

    private Date timestamp = new Date();
    private BigDecimal totalPrice = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private BigDecimal totalDiscount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private Discounts discounts = new Discounts();
    private Map<Item, Integer> items = new HashMap<>();
    private Item lastAddedItem;

    public Date getTimestamp() {
        return timestamp;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public Item getLastAddedItem() {
        return lastAddedItem;
    }

    public Map<Item, Integer> getItems() {
        return items;
    }

    public Discounts getDiscounts() {
        return discounts;
    }


    public void addItem(Item item) {
        this.lastAddedItem = item;
        totalPrice = totalPrice.add(item.getPrice());
        items.compute(item, (itemKey, oldValue) -> oldValue == null ? 1 : oldValue + 1);
    }
}
