package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.receipt.Receipt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Service
public class ManualDiscountProcessor {

    BigDecimal calculateTotalDiscount(Receipt receipt) {
        BigDecimal totalDiscount = BigDecimal.ZERO;
        for (ManualDiscount manualDiscount : receipt.getDiscounts().getAppliedManualDiscounts()) {
            if (manualDiscount.getPercentageDiscount() != null) {
                totalDiscount = totalDiscount.add(getPercentageDiscount(manualDiscount.getPercentageDiscount(), receipt
                        .getTotalPrice()));
            } else if (manualDiscount.getAmountDiscount() != null) {
                totalDiscount = totalDiscount.add(manualDiscount.getAmountDiscount());
            }
        }
        return totalDiscount;
    }

    private BigDecimal getPercentageDiscount(double percentage, BigDecimal totalPrice) {
        return totalPrice.multiply(BigDecimal.valueOf(percentage)).setScale(2, RoundingMode.HALF_UP);

    }
}
