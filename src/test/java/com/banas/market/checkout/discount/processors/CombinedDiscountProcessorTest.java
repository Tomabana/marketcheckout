package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.receipt.Receipt;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CombinedDiscountProcessorTest {

    private Map<Item, Integer> items = new HashMap<>();
    private CombinedDiscountProcessor combinedDiscountProcessor = new CombinedDiscountProcessor();

    @Before
    public void setUp() {
        this.items.put(DataGenerator.ITEM_A, 2);
        this.items.put(DataGenerator.ITEM_B, 3);
        this.items.put(DataGenerator.ITEM_C, 2);
    }

    @Test
    public void discountShouldApply() {
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setItems(new HashSet<>(Arrays.asList(DataGenerator.ITEM_A, DataGenerator.ITEM_B,
                DataGenerator.ITEM_C)));

        boolean canApply = combinedDiscountProcessor.checkIfDiscountCanApply(items, combinedDiscount);

        Assert.assertTrue("Discount should be applied", canApply);
    }

    @Test
    public void discountShouldNotApply() {
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setItems(new HashSet<>(Arrays.asList(DataGenerator.ITEM_A, DataGenerator
                .ITEM_B, DataGenerator.ITEM_C, DataGenerator.ITEM_D)));

        boolean canApply = combinedDiscountProcessor.checkIfDiscountCanApply(items, combinedDiscount);

        Assert.assertFalse("Discount should not be applied", canApply);
    }

    @Test
    public void removeItemsUsedForDiscount() {
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setItems(new HashSet<>(Arrays.asList(DataGenerator.ITEM_A, DataGenerator.ITEM_B,
                DataGenerator.ITEM_C)));
        Map<Item, Integer> basketCopy = new HashMap<>(items);

        combinedDiscountProcessor.removeItemsUsedForDiscount(basketCopy, combinedDiscount);

        Assert.assertTrue("ITEM_A should be removed",
                basketCopy.get(DataGenerator.ITEM_A) == 1);
        Assert.assertTrue("ITEM_B should be removed",
                basketCopy.get(DataGenerator.ITEM_B) == 2);
        Assert.assertTrue("ITEM_C should be removed",
                basketCopy.get(DataGenerator.ITEM_C) == 1);
    }

    @Test
    public void calculateTotalDiscount() {
        BigDecimal discountAmount = BigDecimal.valueOf(50);
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setDiscount(discountAmount);
        Receipt receipt = new Receipt();
        receipt.getDiscounts().getAppliedCombinedDiscounts().add(combinedDiscount);

        BigDecimal totalDiscount = combinedDiscountProcessor.calculateTotalDiscount(receipt.getDiscounts());

        Assert.assertTrue("Discount should be included in total discount",
                totalDiscount.compareTo(discountAmount) == 0);
    }

}