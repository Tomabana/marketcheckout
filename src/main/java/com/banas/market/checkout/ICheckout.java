package com.banas.market.checkout;

import com.banas.market.checkout.discount.model.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.payment.IPayment;
import com.banas.market.checkout.receipt.Receipt;

public interface ICheckout {

    void startNewReceipt();

    Item scanItem(String bardCode);

    void addItem(Item item);

    void addManualDiscount(ManualDiscount discount);

    void pay(IPayment payment);

    Receipt printReceipt();
}
