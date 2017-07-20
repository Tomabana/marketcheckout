package com.banas.market.checkout.discount.processors;

import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.receipt.Receipt;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ManualDiscountProcessorTest {

    private ManualDiscountProcessor manualDiscountProcessor = new ManualDiscountProcessor();

    @Test
    public void calculatePercentageDiscount() {
        Receipt receipt = new Receipt();
        receipt.setTotalPrice(BigDecimal.valueOf(1000));
        ManualDiscount manualPercentageDiscount = new ManualDiscount(0.1d);
        receipt.getDiscounts().getAppliedManualDiscounts().add(manualPercentageDiscount);

        BigDecimal totalDiscount = manualDiscountProcessor.calculateTotalDiscount(receipt);

        Assert.assertTrue("Discount should be applied", totalDiscount.compareTo(BigDecimal.valueOf(100)) == 0);
    }

    @Test
    public void calculateAmountDiscount() {
        Receipt receipt = new Receipt();
        receipt.setTotalPrice(BigDecimal.valueOf(1000));
        BigDecimal amountDiscount = BigDecimal.valueOf(200);
        ManualDiscount manualAmountDiscount = new ManualDiscount(amountDiscount);
        receipt.getDiscounts().getAppliedManualDiscounts().add(manualAmountDiscount);

        BigDecimal totalDiscount = manualDiscountProcessor.calculateTotalDiscount(receipt);

        Assert.assertTrue("Discount should be applied", totalDiscount.compareTo(amountDiscount) == 0);
    }

}