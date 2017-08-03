package com.banas.market.checkout.receipt;

import com.banas.market.checkout.discount.CombinedDiscount;
import com.banas.market.checkout.discount.DiscountPolicy;
import com.banas.market.checkout.discount.ManualDiscount;
import com.banas.market.checkout.discount.QuantityDiscount;
import com.banas.market.checkout.inventory.Item;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "RECEIPT")
@NoArgsConstructor
public class Receipt {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receipt.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TOTAL_PRICE", nullable = false)
    private BigDecimal totalPrice = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);

    @Column(name = "TOTAL_DISCOUNT", nullable = false)
    private BigDecimal totalDiscount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_HALF_EVEN);

    @Column(name = "TIMESTAMP", nullable = false)
    private Date date = new Date();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receipt", cascade = CascadeType.ALL)
    private Set<ReceiptItem> receiptItems = new HashSet<>();

    @Transient
    private List<ManualDiscount> manualDiscounts = new ArrayList<>();

    @Transient
    private List<CombinedDiscount> combinedDiscounts = new ArrayList<>();

    @Transient
    private List<QuantityDiscount> quantityDiscounts = new ArrayList<>();

    @Transient
    private Map<Item, Integer> receiptItemsWithNotAppliedDiscount = new HashMap<>();

    @Transient
    private Item lastAddedItem;

    @Transient
    private DiscountPolicy discountPolicy;

    public Receipt(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void addItem(@NonNull Item item) {
        lastAddedItem = item;
        receiptItemsWithNotAppliedDiscount.putIfAbsent(item, 0);
        receiptItemsWithNotAppliedDiscount.compute(item, (key, value) -> value + 1);
        ReceiptItem foundItem = foundItemOrAddNew(item);
        foundItem.increaseQuantity();
        totalPrice = totalPrice.add(item.getPrice());
        discountPolicy.addNewDiscounts(this);
    }

    public void addCombinedDiscount(@NonNull CombinedDiscount combinedDiscount) {
        combinedDiscounts.add(combinedDiscount);
        totalDiscount = totalDiscount.add(combinedDiscount.getDiscount());
        for (Item item : combinedDiscount.getDiscountItems()) {
            receiptItemsWithNotAppliedDiscount.compute(item, (itemKey, quantity) -> quantity - 1);
        }
    }

    public void addQuantityDiscount(@NonNull QuantityDiscount quantityDiscount) {
        quantityDiscounts.add(quantityDiscount);
        totalDiscount = totalDiscount.add(quantityDiscount.getDiscount());
        receiptItemsWithNotAppliedDiscount.compute(quantityDiscount.getItem(),
                (itemKey, quantity) -> quantity - quantityDiscount.getQuantity());
    }

    public void addManualDiscount(@NonNull ManualDiscount manualDiscount) {
        manualDiscounts.add(manualDiscount);
        totalDiscount = totalDiscount.add(manualDiscount.getDiscount(totalPrice));
    }

    public boolean isPercentageDiscountApplied(@NonNull ManualDiscount manualDiscount) {
        return manualDiscounts.contains(manualDiscount);
    }

    public Map<Item, Integer> getReceiptItemsWithNotAppliedDiscount() {
        return Collections.unmodifiableMap(receiptItemsWithNotAppliedDiscount);
    }

    public Item getLastAddedItem() {
        return lastAddedItem;
    }

    public BigDecimal getTotalPriceIncludingDiscounts() {
        return totalPrice.subtract(totalDiscount);
    }

    public void print() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Date = ").append(date).append(System.lineSeparator());
        stringBuilder.append("Items:").append(System.lineSeparator());
        receiptItems.forEach(receiptItem -> stringBuilder.append(receiptItem).append(System.lineSeparator()));
        stringBuilder.append("Combined discounts:").append(System.lineSeparator());
        combinedDiscounts.forEach(combinedDiscount -> stringBuilder.append(combinedDiscount)
                .append(System.lineSeparator()));
        stringBuilder.append("Quantity discounts:").append(System.lineSeparator());
        quantityDiscounts.forEach(quantityDiscount -> stringBuilder.append(quantityDiscount)
                .append(System.lineSeparator()));
        stringBuilder.append("Manual discounts:").append(System.lineSeparator());
        manualDiscounts.forEach(manualDiscount -> stringBuilder.append(manualDiscount).append(System.lineSeparator()));
        stringBuilder.append("Total price without discounts: " + totalPrice + System.lineSeparator());
        stringBuilder.append("Total discount: ").append(totalDiscount).append(System.lineSeparator());
        stringBuilder.append("Total price with applied discounts ").append(totalPrice.subtract(totalDiscount));

        LOGGER.info(stringBuilder.toString());
    }

    private ReceiptItem foundItemOrAddNew(Item item) {
        return receiptItems.stream()
                .filter(receiptItem -> item.equals(receiptItem.getItem()))
                .findFirst().orElseGet(() -> {
                    ReceiptItem receiptItem = new ReceiptItem(this, item);
                    receiptItems.add(receiptItem);
                    return receiptItem;
                });
    }
}