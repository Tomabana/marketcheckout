package com.banas.market.checkout.discount.model;

import com.banas.market.checkout.discount.entities.CombinedDiscount;
import com.banas.market.checkout.discount.entities.QuantityDiscount;

import java.util.ArrayList;
import java.util.List;

public class Discounts {
    private List<CombinedDiscount> possibleCombinedDiscounts = new ArrayList<>();
    private List<QuantityDiscount> possibleQuantityDiscounts = new ArrayList<>();
    private List<QuantityDiscount> appliedQuantityDiscounts = new ArrayList<>();
    private List<CombinedDiscount> appliedCombinedDiscounts = new ArrayList<>();
    private List<ManualDiscount> appliedManualDiscounts = new ArrayList<>();

    public Discounts() {
    }

    public Discounts(List<CombinedDiscount> possibleCombinedDiscounts, List<QuantityDiscount> possibleQuantityDiscounts) {
        this.possibleCombinedDiscounts = new ArrayList<>(possibleCombinedDiscounts);
        this.possibleQuantityDiscounts = new ArrayList<>(possibleQuantityDiscounts);
    }

    public List<CombinedDiscount> getPossibleCombinedDiscounts() {
        return possibleCombinedDiscounts;
    }

    public List<QuantityDiscount> getPossibleQuantityDiscounts() {
        return possibleQuantityDiscounts;
    }

    public List<QuantityDiscount> getAppliedQuantityDiscounts() {
        return appliedQuantityDiscounts;
    }

    public void setAppliedQuantityDiscounts(List<QuantityDiscount> appliedQuantityDiscounts) {
        this.appliedQuantityDiscounts = appliedQuantityDiscounts;
    }

    public List<CombinedDiscount> getAppliedCombinedDiscounts() {
        return appliedCombinedDiscounts;
    }

    public void setAppliedCombinedDiscounts(List<CombinedDiscount> appliedCombinedDiscounts) {
        this.appliedCombinedDiscounts = appliedCombinedDiscounts;
    }

    public List<ManualDiscount> getAppliedManualDiscounts() {
        return appliedManualDiscounts;
    }

}
