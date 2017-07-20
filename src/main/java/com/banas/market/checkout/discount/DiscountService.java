package com.banas.market.checkout.discount;

import com.banas.market.checkout.discount.processors.DiscountProcessor;
import com.banas.market.checkout.receipt.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class DiscountService {

    @Autowired
    private DiscountProcessor discountProcessor;

    public Receipt applyBestPossibleDiscounts(Receipt receipt) {
        return discountProcessor.applyBestPossibleDiscounts(receipt);
    }
}
