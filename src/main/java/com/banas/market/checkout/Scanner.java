package com.banas.market.checkout;

import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Scope("prototype")
public class Scanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Checkout.class);

    @Autowired
    private ItemRepository itemRepository;

    public Item scanItem(String barcode) {
        LOGGER.info("Scanning item with barCode = {}", barcode);
        return itemRepository.findByBarcode(barcode);
    }
}