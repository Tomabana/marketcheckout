package com.banas.market.checkout.discount;

import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import com.banas.market.checkout.receipt.Receipt;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Service
public class DiscountPolicy {

    private static final BigDecimal PERCENTAGE_DISCOUNT_PRICE = new BigDecimal(600);
    private static final ManualDiscount PERCENTAGE_DISCOUNT = new ManualDiscount(0.1d);

    @Autowired
    private ItemRepository itemRepository;

    //Cached discounts
    //we don't have to retrieve all discounts from database each time when we want to calculate new discounts
    private Set<CombinedDiscount> possibleCombinedDiscount = new CopyOnWriteArraySet<>();
    private Set<QuantityDiscount> possibleQuantityDiscount = new CopyOnWriteArraySet<>();

    public void addNewDiscounts(@NonNull Receipt receipt) {
        if (receipt.getLastAddedItem() != null) {
            cachedPossibleDiscounts(receipt.getLastAddedItem());
        }
        Map<Item, Integer> items = receipt.getReceiptItemsWithNotAppliedDiscount();
        possibleCombinedDiscount.forEach(combinedDiscount -> {
            if (combinedDiscount.checkIfDiscountCanApply(items)) {
                receipt.addCombinedDiscount(combinedDiscount);
            }
        });
        possibleQuantityDiscount.forEach(quantityDiscount -> {
            if (quantityDiscount.checkIfDiscountCanApply(items)) {
                receipt.addQuantityDiscount(quantityDiscount);
            }
        });
        addPercentageDiscount(receipt);
    }

    private void cachedPossibleDiscounts(Item lastAddedItem) {
        Item itemWithDiscounts = itemRepository.findByIdWithDiscounts(lastAddedItem.getId());
        possibleQuantityDiscount.addAll(itemWithDiscounts.getQuantityDiscounts());
        possibleCombinedDiscount.addAll(itemWithDiscounts.getCombinedDiscounts());
    }

    private void addPercentageDiscount(Receipt receipt) {
        boolean totalPriceIsGreaterThanLowerLevelOfPercentageDiscount = receipt.getTotalPriceIncludingDiscounts()
                .compareTo(PERCENTAGE_DISCOUNT_PRICE) == 1;
        if (totalPriceIsGreaterThanLowerLevelOfPercentageDiscount && !receipt.isPercentageDiscountApplied
                (PERCENTAGE_DISCOUNT)) {
            receipt.addManualDiscount(PERCENTAGE_DISCOUNT);
        }
    }

}
