package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.entities.QuantityDiscount;
import com.banas.market.checkout.inventory.Item;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

class DataGenerator {

    private enum ItemData {
        ITEM_A(1, "ItemA", BigDecimal.valueOf(50), 3, BigDecimal.valueOf(100)),
        ITEM_B(2, "ItemB", BigDecimal.valueOf(100), 2, BigDecimal.valueOf(175)),
        ITEM_C(3, "ItemC", BigDecimal.valueOf(40), 4, BigDecimal.valueOf(140)),
        ITEM_D(4, "ItemD", BigDecimal.valueOf(25), 2, BigDecimal.valueOf(45));

        private long id;
        private String name;
        private BigDecimal price;
        private int quantityDiscountUnits;
        private BigDecimal quantityDiscountPrice;

        ItemData(long id, String name, BigDecimal price, int quantityDiscountUnits, BigDecimal
                quantityDiscountPrice) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.quantityDiscountUnits = quantityDiscountUnits;
            this.quantityDiscountPrice = quantityDiscountPrice;
        }
    }

    private enum CombinedDiscountData {
        ABCD(Arrays.asList(ITEM_A, ITEM_B, ITEM_C, ITEM_D), BigDecimal.valueOf(100)),
        AB(Arrays.asList(ITEM_A, ITEM_B), BigDecimal.valueOf(20)),
        AC(Arrays.asList(ITEM_A, ITEM_C), BigDecimal.valueOf(15));

        private Set<Item> items;
        private BigDecimal discount;

        CombinedDiscountData(List<Item> items, BigDecimal discount) {
            this.items = new HashSet<>(items);
            this.discount = discount;
        }
    }


    static final Item ITEM_A = generateItem(ItemData.ITEM_A);
    static final Item ITEM_B = generateItem(ItemData.ITEM_B);
    static final Item ITEM_C = generateItem(ItemData.ITEM_C);
    static final Item ITEM_D = generateItem(ItemData.ITEM_D);

    static {
        addDiscountsToItems();
    }


    private static void addDiscountsToItems() {
        Stream.of(CombinedDiscountData.values())
                .forEach(combinedDiscountData -> addCombinedDiscountToItems(combinedDiscountData));
    }

    private static Item generateItem(ItemData itemData) {
        Item item = new Item();
        item.setId(itemData.id);
        item.setName(itemData.name);
        item.setPrice(itemData.price);
        item.setQuantityDiscounts(new HashSet<>());
        item.setCombinedDiscounts(new HashSet<>());

        QuantityDiscount quantityDiscount = new QuantityDiscount();
        quantityDiscount.setItem(item);
        quantityDiscount.setQuantity(itemData.quantityDiscountUnits);
        quantityDiscount.setPrice(itemData.quantityDiscountPrice);

        item.getQuantityDiscounts().add(quantityDiscount);

        return item;
    }

    private static void addCombinedDiscountToItems(CombinedDiscountData combinedDiscountData) {
        CombinedDiscount combinedDiscount = new CombinedDiscount();
        combinedDiscount.setItems(combinedDiscountData.items);
        combinedDiscount.setDiscount(combinedDiscountData.discount);
        combinedDiscountData.items.stream().forEach(item -> item.getCombinedDiscounts().add(combinedDiscount));
    }
}