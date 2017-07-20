package com.banas.market.checkout.payment;


import com.banas.market.checkout.receipt.Receipt;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public class CashPaymentService implements IPayment {

    @Override
    public boolean pay(Receipt receipt) {
        return true;
    }
}
