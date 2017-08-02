package com.banas.market.checkout.discount;

import com.banas.market.checkout.inventory.Item;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "COMBINED_DISCOUNT")
@EqualsAndHashCode
public class CombinedDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter
    @Column(name = "DISCOUNT", nullable = false)
    private BigDecimal discount;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable
            (name = "COMBINED_DISCOUNT_ITEM",
                    joinColumns = @JoinColumn(name = "COMBINED_DISCOUNT_ID", nullable = false),
                    inverseJoinColumns = @JoinColumn(name = "ITEM_ID", nullable = false))
    public Set<Item> discountItems;

    boolean checkIfDiscountCanApply(Map<Item, Integer> itemsInBasket) {
        boolean canApply = false;
        for (Item item : discountItems) {
            if (itemsInBasket.get(item) != null && itemsInBasket.get(item) > 0) {
                canApply = true;
            } else {
                canApply = false;
                break;
            }
        }
        return canApply;
    }

    @Override
    public String toString() {
        String items = discountItems.stream().map(item -> item.getName()).reduce((acc, actual) -> acc + " " + actual).get();
        return "Combined discount{" +
                "discount=" + discount +
                ", items=" + items +
                '}';
    }
}
