package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.QuantityDiscount;
import com.banas.market.checkout.discount.model.Discounts;
import com.banas.market.checkout.inventory.Item;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Service
class QuantityDiscountProcessor {

    boolean checkIfDiscountCanApply(Map<Item, Integer> items, QuantityDiscount quantityDiscount) {
        return items.get(quantityDiscount.getItem()) != null && items.get(quantityDiscount.getItem()) >=
                quantityDiscount.getQuantity();
    }

    Map<Item, Integer> removeItemsUsedForDiscount(Map<Item, Integer> items, QuantityDiscount quantityDiscount) {
        items.compute(quantityDiscount.getItem(), (itemKey, quantity) -> quantity - quantityDiscount.getQuantity());
        return items;
    }

    BigDecimal calculateTotalDiscount(Discounts discounts) {
        return discounts.getAppliedQuantityDiscounts().stream()
                .map(quantityDiscount -> getDiscount(quantityDiscount))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getDiscount(QuantityDiscount quantityDiscount) {
        BigDecimal priceWithoutDiscount = quantityDiscount.getItem().getPrice().multiply(BigDecimal.valueOf
                (quantityDiscount.getQuantity()));
        return priceWithoutDiscount.subtract(quantityDiscount.getPrice());
    }

}
