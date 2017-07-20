package com.banas.market.checkout.payment;

import com.banas.market.checkout.receipt.Receipt;

public interface IPayment {

    boolean pay(Receipt receipt);
}
