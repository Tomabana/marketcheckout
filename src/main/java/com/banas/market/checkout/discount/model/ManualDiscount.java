package com.banas.market.checkout.discount.model;

import org.springframework.util.Assert;

import java.math.BigDecimal;

public class ManualDiscount {
    private BigDecimal amountDiscount;
    private Double percentageDiscount;

    public ManualDiscount(BigDecimal amountDiscount) {
        this.amountDiscount = amountDiscount;
    }

    public ManualDiscount(Double percentageDiscount) {
        Assert.isTrue(percentageDiscount != null || percentageDiscount > 0 || percentageDiscount < 1,
                "Percentage value should be between (0 - 1)");
        this.percentageDiscount = percentageDiscount;
    }

    public BigDecimal getAmountDiscount() {
        return amountDiscount;
    }

    public Double getPercentageDiscount() {
        return percentageDiscount;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ManualDiscount {");
        if (amountDiscount != null) {
            result.append("amountDiscount=" + amountDiscount);
        } else if (percentageDiscount != null) {
            result.append("percentageDiscount=" + percentageDiscount);
        }
        result.append("}");
        return result.toString();
    }
}
