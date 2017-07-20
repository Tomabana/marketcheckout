package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.QuantityDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.receipt.Receipt;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class QuantityDiscountProcessorTest {

    private Map<Item, Integer> items = new HashMap<>();
    private DataGenerator dataGenerator = new DataGenerator();
    private QuantityDiscountProcessor quantityDiscountProcessor = new QuantityDiscountProcessor();

    @Before
    public void setUp() {
        this.items.put(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A), 4);
        this.items.put(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_B), 3);
        this.items.put(dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_C), 1);
    }

    @Test
    public void discountShouldApply() {
        Item item = dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A);

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(3);

        Assert.assertTrue("Discount should be applied", quantityDiscountProcessor.checkIfDiscountCanApply(items,
                quantityDiscount));
    }

    @Test
    public void discountShouldNotApply() {
        Item item = dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A);

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(2);

        Assert.assertTrue("Discount shouldn't be applied", quantityDiscountProcessor.checkIfDiscountCanApply(items,
                quantityDiscount));
    }

    @Test
    public void removeItemsUsedForDiscount() {
        Item item = dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A);

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(3);

        Map<Item, Integer> basketCopy = new HashMap<>(items);
        quantityDiscountProcessor.removeItemsUsedForDiscount(basketCopy, quantityDiscount);
        Assert.assertTrue("Items should be removed", basketCopy.get(item) == 1);
    }

    @Test
    public void calculateTotalDiscount() {
        Item item = dataGenerator.getItem(DataGenerator.ItemName.PRODUCT_A);

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(3);
        quantityDiscount.setPrice(BigDecimal.valueOf(100));

        Receipt receipt = new Receipt();
        receipt.getDiscounts().getAppliedQuantityDiscounts().add(quantityDiscount);
        BigDecimal totalDiscount = quantityDiscountProcessor.calculateTotalDiscount(receipt.getDiscounts());
        Assert.assertTrue("Discount should be included in total discount",
                totalDiscount.compareTo(BigDecimal.valueOf(50)) == 0);
    }
}