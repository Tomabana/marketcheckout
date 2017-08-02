package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.Scanner;
import com.banas.market.checkout.inventory.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

class CheckoutRunner implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutRunner.class);

    private Checkout checkout;
    private Scanner scanner;
    private ArrayBlockingQueue<List<Item>> basketsQueue;

    CheckoutRunner(Checkout checkout, Scanner scanner, ArrayBlockingQueue<List<Item>> basketsQueue) {
        this.checkout = checkout;
        this.basketsQueue = basketsQueue;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        List<Item> items;
        while ((items = basketsQueue.poll()) != null) {
            LOGGER.info("Took basket from queue.");
            this.run(items);
        }
        LOGGER.info("Queue is empty. Closing checkout.");
    }

    private void run(List<Item> items) {
        checkout.startNewReceipt();
        items.forEach(item -> checkout.addItem(scanner.scanItem(item.getBarcode())));
        checkout.pay();
        checkout.printReceipt();
    }
}
