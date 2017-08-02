package com.banas.market.checkout.discount;

import lombok.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ManualDiscount {
    private BigDecimal amountDiscount;
    private Double percentageDiscount;

    public ManualDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public ManualDiscount(@NonNull Double percentageDiscount) {
        if (percentageDiscount <= 0 || percentageDiscount > 1) {
            throw new IllegalArgumentException("Percentage value should be between <0 - 1)");
        }
        this.percentageDiscount = percentageDiscount;
    }

    public BigDecimal getDiscount(@NonNull BigDecimal basketTotalPrice) {
        BigDecimal discount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        if (percentageDiscount != null) {
            discount = discount.add(basketTotalPrice.multiply(BigDecimal.valueOf(percentageDiscount)));
        } else if (amountDiscount != null) {
            discount = amountDiscount.compareTo(basketTotalPrice) > 0 ? basketTotalPrice : amountDiscount;
        }
        return discount;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ManualDiscount {");
        if (amountDiscount != null) {
            result.append("amountDiscount=").append(amountDiscount);
        } else if (percentageDiscount != null) {
            result.append("percentageDiscount=").append(percentageDiscount);
        }
        result.append("}");
        return result.toString();
    }
}
