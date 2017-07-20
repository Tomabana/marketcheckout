package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.model.Discounts;
import com.banas.market.checkout.inventory.Item;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Service
class CombinedDiscountProcessor {

    boolean checkIfDiscountCanApply(Map<Item, Integer> items, CombinedDiscount combinedDiscount) {
        boolean canApply = false;
        for (Item item : combinedDiscount.getItems()) {
            if (items.get(item) != null && items.get(item) > 0) {
                canApply = true;
            } else {
                canApply = false;
                break;
            }
        }
        return canApply;
    }

    Map<Item, Integer> removeItemsUsedForDiscount(Map<Item, Integer> items, CombinedDiscount combinedDiscount) {
        for (Item item : combinedDiscount.getItems()) {
            items.compute(item, (itemKey, quantity) -> quantity - 1);
        }
        return items;
    }

    BigDecimal calculateTotalDiscount(Discounts discounts) {
        return discounts.getAppliedCombinedDiscounts().stream()
                .map(combinedDiscount -> combinedDiscount.getDiscount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
