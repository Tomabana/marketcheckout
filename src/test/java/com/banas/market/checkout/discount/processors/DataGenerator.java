package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.entities.QuantityDiscount;
import com.banas.market.checkout.inventory.Item;

import java.math.BigDecimal;
import java.util.*;

class DataGenerator {

    public enum ItemName {
        PRODUCT_A, PRODUCT_B, PRODUCT_C, PRODUCT_D
    }

    private Map<ItemName, Item> itemsMap = new HashMap<>();

    DataGenerator() {
        generateItemsWithDiscounts();
    }

    Item getItem(ItemName itemName) {
        return itemsMap.get(itemName);
    }

    private void generateItemsWithDiscounts() {
        itemsMap.put(ItemName.PRODUCT_A, generateItemA());
        itemsMap.put(ItemName.PRODUCT_B, generateItemB());
        itemsMap.put(ItemName.PRODUCT_C, generateItemC());
        itemsMap.put(ItemName.PRODUCT_D, generateItemD());

        Set<Item> combinedDiscountABCD = new HashSet<>(Arrays.asList(itemsMap.get(ItemName.PRODUCT_A),
                itemsMap.get(ItemName.PRODUCT_B), itemsMap.get(ItemName.PRODUCT_C),
                itemsMap.get(ItemName.PRODUCT_D)));
        Set<Item> combinedDiscountAB = new HashSet<>(Arrays.asList(itemsMap.get(ItemName.PRODUCT_A),
                itemsMap.get(ItemName.PRODUCT_B)));
        Set<Item> combinedDiscountAC = new HashSet<>(Arrays.asList(itemsMap.get(ItemName.PRODUCT_A),
                itemsMap.get(ItemName.PRODUCT_C)));

        addCombinedDiscountToItems(combinedDiscountABCD, BigDecimal.valueOf(100));
        addCombinedDiscountToItems(combinedDiscountAB, BigDecimal.valueOf(20));
        addCombinedDiscountToItems(combinedDiscountAC, BigDecimal.valueOf(15));

    }

    private Item generateItemA() {
        Item item = new Item();
        item.setId(1L);
        item.setName("ItemA");
        item.setPrice(BigDecimal.valueOf(50));
        item.setQuantityDiscounts(new HashSet<>());
        item.setCombinedDiscounts(new HashSet<>());
        item.getQuantityDiscounts().add(generateQuantityDiscount(item, 3, BigDecimal.valueOf(100)));

        return item;
    }

    private Item generateItemB() {
        Item item = new Item();
        item.setId(2L);
        item.setName("ItemB");
        item.setPrice(BigDecimal.valueOf(100));
        item.setQuantityDiscounts(new HashSet<>());
        item.setCombinedDiscounts(new HashSet<>());
        item.getQuantityDiscounts().add(generateQuantityDiscount(item, 2, BigDecimal.valueOf(175)));

        return item;
    }

    private Item generateItemC() {
        Item item = new Item();
        item.setId(3L);
        item.setName("ItemC");
        item.setPrice(BigDecimal.valueOf(40));
        item.setQuantityDiscounts(new HashSet<>());
        item.setCombinedDiscounts(new HashSet<>());
        item.getQuantityDiscounts().add(generateQuantityDiscount(item, 4, BigDecimal.valueOf(140)));

        return item;
    }

    private Item generateItemD() {
        Item item = new Item();
        item.setId(4L);
        item.setName("ItemD");
        item.setPrice(BigDecimal.valueOf(25));
        item.setQuantityDiscounts(new HashSet<>());
        item.setCombinedDiscounts(new HashSet<>());
        item.getQuantityDiscounts().add(generateQuantityDiscount(item, 2, BigDecimal.valueOf(45)));

        return item;
    }

    private QuantityDiscount generateQuantityDiscount(Item item, Integer quantity, BigDecimal price) {
        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(quantity);
        quantityDiscount.setPrice(price);

        return quantityDiscount;
    }

    private void addCombinedDiscountToItems(Set<Item> items, BigDecimal quantity) {
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setItems(items);
        combinedDiscount.setDiscount(quantity);
        items.stream().forEach(item -> item.getCombinedDiscounts().add(combinedDiscount));
    }

}
