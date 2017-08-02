package com.banas.market.checkout.inventory;

import com.banas.market.checkout.discount.CombinedDiscount;
import com.banas.market.checkout.discount.QuantityDiscount;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "ITEM")
@EqualsAndHashCode(of = {"id", "name", "barcode"})
@ToString(of = "name")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;

    @Getter
    @Column(name = "NAME", nullable = false)
    private String name;

    @Getter
    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Getter
    @Column(name = "BARCODE", nullable = false)
    private String barcode;

    @Getter
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<QuantityDiscount> quantityDiscounts;

    @Getter
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "discountItems")
    private Set<CombinedDiscount> combinedDiscounts;
}
