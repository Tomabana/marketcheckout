package com.banas.market.checkout.receipt;

import com.banas.market.checkout.discount.DiscountPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class ReceiptFactory {

    @Autowired
    private DiscountPolicy discountPolicy;

    public Receipt createReceipt() {
        return new Receipt(discountPolicy);
    }
}
