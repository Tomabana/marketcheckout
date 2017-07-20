package com.banas.market.checkout.inventory;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.entities.QuantityDiscount;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "BARCODE")
    private String barcode;

    @Column(name = "PRICE")
    private BigDecimal price;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private Set<QuantityDiscount> quantityDiscounts;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<CombinedDiscount> combinedDiscounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<QuantityDiscount> getQuantityDiscounts() {
        return quantityDiscounts;
    }

    public void setQuantityDiscounts(Set<QuantityDiscount> quantityDiscounts) {
        this.quantityDiscounts = quantityDiscounts;
    }

    public Set<CombinedDiscount> getCombinedDiscounts() {
        return combinedDiscounts;
    }

    public void setCombinedDiscounts(Set<CombinedDiscount> combinedDiscounts) {
        this.combinedDiscounts = combinedDiscounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != null ? !id.equals(item.id) : item.id != null) return false;
        if (name != null ? !name.equals(item.name) : item.name != null) return false;
        return barcode != null ? barcode.equals(item.barcode) : item.barcode == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (barcode != null ? barcode.hashCode() : 0);
        return result;
    }
}
