package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.payment.IPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;

class CheckoutRunner implements Runnable {
    private static Logger LOGGER = LoggerFactory.getLogger(CheckoutRunner.class);

    private IPayment payment;
    private Checkout checkout;
    private ArrayBlockingQueue<Basket> basketsQueue;

    CheckoutRunner(Checkout checkout, ArrayBlockingQueue<Basket> basketsQueue, IPayment payment) {
        this.checkout = checkout;
        this.basketsQueue = basketsQueue;
        this.payment = payment;
    }

    @Override
    public void run() {
        Basket basket;
        while ((basket = basketsQueue.poll()) != null) {
            LOGGER.info("Took basket from queue.");
            this.run(basket);
        }
        LOGGER.info("Queue is empty. Closing checkout.");
    }

    private void run(Basket basket) {
        checkout.startNewReceipt();
        for (Item item : basket.getItems()) {
            checkout.scanItem(item.getBarcode());
        }
        checkout.pay(payment);
        checkout.printReceipt();
    }
}
