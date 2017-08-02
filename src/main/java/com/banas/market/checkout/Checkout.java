package com.banas.market.checkout;

import com.banas.market.checkout.discount.ManualDiscount;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.receipt.Receipt;
import com.banas.market.checkout.receipt.ReceiptFactory;
import com.banas.market.checkout.receipt.ReceiptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
@Service
@Scope("prototype")
public class Checkout {

    private static final Logger LOGGER = LoggerFactory.getLogger(Checkout.class);
    private boolean paymentDone = false;
    private Receipt receipt;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ReceiptFactory receiptFactory;

    @PostConstruct
    public void startNewReceipt() {
        LOGGER.info("Starting new receipt");
        receipt = receiptFactory.createReceipt();
    }

    public void addItem(Item item) {
        LOGGER.info("Adding item to receipt");
        receipt.addItem(item);
    }

    public void addManualDiscount(ManualDiscount manualDiscount) {
        receipt.addManualDiscount(manualDiscount);
    }

    public void pay() {
        paymentDone = true;
    }

    public Receipt printReceipt() {
        if (paymentDone) {
            LOGGER.info("Printing receipt.");
            receipt.print();
            receiptRepository.save(receipt);
        } else {
            LOGGER.warn("The receipt is not paid. Client has to pay receipt before printing it out.");
        }
        return receipt;
    }
}
