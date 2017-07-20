package com.banas.market.checkout.discount.entities;

import com.banas.market.checkout.inventory.Item;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "COMBINED_DISCOUNT")
public class CombinedDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "DISCOUNT")
    private BigDecimal discount;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable
            (name = "COMBINED_DISCOUNT_ITEM",
                    joinColumns = @JoinColumn(name = "COMBINED_DISCOUNT_ID"),
                    inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    public Set<Item> items;

    public Long getId() {
        return id;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CombinedDiscount that = (CombinedDiscount) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (discount != null ? !discount.equals(that.discount) : that.discount != null) return false;
        return items != null ? items.equals(that.items) : that.items == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (discount != null ? discount.hashCode() : 0);
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String items = getItems().stream().map(item -> item.getName()).reduce((acc, actual) -> acc + actual).get();
        return "Combined discount{" +
                "discount=" + discount +
                ", items=" + items +
                '}';
    }
}
