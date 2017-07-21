package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.entities.QuantityDiscount;
import com.banas.market.checkout.discount.model.Discounts;
import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemService;
import com.banas.market.checkout.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Service
public class DiscountProcessor {

    private static final BigDecimal PERCENTAGE_DISCOUNT_PRICE = new BigDecimal(600);
    private static final ManualDiscount PERCENTAGE_DISCOUNT = new ManualDiscount(0.1d);

    @Autowired
    private ItemService itemService;

    @Autowired
    private ManualDiscountProcessor manualDiscountProcessor;

    @Autowired
    private QuantityDiscountProcessor quantityDiscountProcessor;

    @Autowired
    private CombinedDiscountProcessor combinedDiscountProcessor;

    public Receipt applyBestPossibleDiscounts(Receipt receipt) {
        addNewPossibleDiscounts(receipt);
        //We have to remove percentage discount to calculate everything without this 'special' discount
        receipt.getDiscounts().getAppliedManualDiscounts().remove(PERCENTAGE_DISCOUNT);
        receipt = calculateDiscountPossibilities(receipt);
        receipt = addOrRemovePercentageDiscount(receipt);
        receipt.setTotalDiscount(receipt.getTotalDiscount().add(manualDiscountProcessor.calculateTotalDiscount
                (receipt)));
        return receipt;
    }

    private void addNewPossibleDiscounts(Receipt receipt) {
        //If item of particular type (lastAddedItem type) is added first time add new possible discounts
        if (receipt.getItems().get(receipt.getLastAddedItem()) == 1) {
            Optional<Item> itemWithDiscounts = itemService.getItemWithDiscounts(receipt.getLastAddedItem().getId());
            if (itemWithDiscounts.isPresent()) {
                receipt.getDiscounts().getPossibleQuantityDiscounts()
                        .addAll(itemWithDiscounts.get().getQuantityDiscounts());
                for (CombinedDiscount combinedDiscount : itemWithDiscounts.get().getCombinedDiscounts()) {
                    if (!receipt.getDiscounts().getAppliedQuantityDiscounts().contains(combinedDiscount)
                            && combinedDiscountProcessor.checkIfDiscountCanApply(receipt.getItems(), combinedDiscount)) {
                        receipt.getDiscounts().getPossibleCombinedDiscounts().add(combinedDiscount);
                    }
                }
            }
        }
    }

    private Receipt calculateDiscountPossibilities(Receipt receipt) {
        receipt.setTotalDiscount(BigDecimal.ZERO);
        Discounts discounts = new Discounts(receipt.getDiscounts().getPossibleCombinedDiscounts(),
                receipt.getDiscounts().getPossibleQuantityDiscounts());

        if (discounts.getPossibleCombinedDiscounts().isEmpty() && discounts.getPossibleQuantityDiscounts().isEmpty()) {
            return receipt;
        }

        while (!discounts.getPossibleQuantityDiscounts().isEmpty()) {
            calculateDiscountPossibilities(receipt, new HashMap<>(receipt.getItems()), discounts);
            discounts.getAppliedCombinedDiscounts().clear();
            discounts.getAppliedQuantityDiscounts().clear();
            discounts.getPossibleQuantityDiscounts().remove(0);
        }
        discounts.getPossibleQuantityDiscounts().addAll(receipt.getDiscounts().getPossibleQuantityDiscounts());
        while (!discounts.getPossibleCombinedDiscounts().isEmpty()) {
            calculateDiscountPossibilities(receipt, new HashMap<>(receipt.getItems()), discounts);
            discounts.getAppliedCombinedDiscounts().clear();
            discounts.getAppliedQuantityDiscounts().clear();
            discounts.getPossibleCombinedDiscounts().remove(0);
        }

        return receipt;
    }

    private void calculateDiscountPossibilities(Receipt receipt, Map<Item, Integer> reducedItems, Discounts discounts) {
        calculateQuantityPossibleDiscounts(reducedItems, discounts, 0);
        calculateCombinedPossibleDiscounts(reducedItems, discounts, 0);

        comparePricesAndChooseMinimum(receipt, discounts);
    }

    private void comparePricesAndChooseMinimum(Receipt receipt, Discounts discounts) {
        BigDecimal totalPrice = calculateTotalPrice(receipt.getItems());
        BigDecimal totalDiscount = calculateTotalDiscount(discounts);
        BigDecimal currentMinPrice = receipt.getTotalPrice();
        BigDecimal currentMinDiscount = receipt.getTotalDiscount();
        if (totalPrice.subtract(totalDiscount).compareTo(currentMinPrice.subtract(currentMinDiscount)) == -1) {
            receipt.setTotalPrice(totalPrice);
            receipt.setTotalDiscount(totalDiscount);
            receipt.getDiscounts().setAppliedQuantityDiscounts(
                    new ArrayList<>(discounts.getAppliedQuantityDiscounts()));
            receipt.getDiscounts().setAppliedCombinedDiscounts(
                    new ArrayList<>(discounts.getAppliedCombinedDiscounts()));
        }
    }

    private void calculateQuantityPossibleDiscounts(Map<Item, Integer> reducedItems, Discounts discounts, int index) {
        if (discounts.getPossibleQuantityDiscounts().size() > index) {
            QuantityDiscount quantityDiscount = discounts.getPossibleQuantityDiscounts().get(index);
            if (quantityDiscountProcessor.checkIfDiscountCanApply(reducedItems, quantityDiscount)) {
                reducedItems = quantityDiscountProcessor.removeItemsUsedForDiscount(reducedItems, quantityDiscount);
                discounts.getAppliedQuantityDiscounts().add(quantityDiscount);
                calculateQuantityPossibleDiscounts(reducedItems, discounts, index);
            } else {
                calculateQuantityPossibleDiscounts(reducedItems, discounts, ++index);
            }
        }
    }

    private void calculateCombinedPossibleDiscounts(Map<Item, Integer> reducedItems, Discounts discounts, int index) {
        if (discounts.getPossibleCombinedDiscounts().size() > index) {
            CombinedDiscount combinedDiscount = discounts.getPossibleCombinedDiscounts().get(index);
            if (combinedDiscountProcessor.checkIfDiscountCanApply(reducedItems, combinedDiscount)) {
                reducedItems = combinedDiscountProcessor.removeItemsUsedForDiscount(reducedItems, combinedDiscount);
                discounts.getAppliedCombinedDiscounts().add(combinedDiscount);
                calculateCombinedPossibleDiscounts(reducedItems, discounts, index);
            } else {
                calculateCombinedPossibleDiscounts(reducedItems, discounts, ++index);
            }
        }
    }

    private BigDecimal calculateTotalDiscount(Discounts discounts) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        totalDiscount = totalDiscount.add(quantityDiscountProcessor.calculateTotalDiscount(discounts));
        totalDiscount = totalDiscount.add(combinedDiscountProcessor.calculateTotalDiscount(discounts));
        return totalDiscount;
    }

    private BigDecimal calculateTotalPrice(Map<Item, Integer> items) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            if (entry.getValue() > 0) {
                totalPrice = totalPrice.add(entry.getKey().getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }
        }
        return totalPrice;
    }

    private Receipt addOrRemovePercentageDiscount(Receipt receipt) {
        boolean totalPriceIsGreaterThanLowerLevelOfPercentageDiscount = receipt.getTotalPrice().subtract(receipt
                .getTotalDiscount()).compareTo(PERCENTAGE_DISCOUNT_PRICE) == 1;
        if (totalPriceIsGreaterThanLowerLevelOfPercentageDiscount && !receipt.getDiscounts().getAppliedManualDiscounts
                ().contains(PERCENTAGE_DISCOUNT)) {
            receipt.getDiscounts().getAppliedManualDiscounts().add(PERCENTAGE_DISCOUNT);
        } else if (!totalPriceIsGreaterThanLowerLevelOfPercentageDiscount && receipt.getDiscounts()
                .getAppliedManualDiscounts().contains(PERCENTAGE_DISCOUNT)) {
            receipt.getDiscounts().getAppliedManualDiscounts().remove(PERCENTAGE_DISCOUNT);
        }
        return receipt;
    }

}
