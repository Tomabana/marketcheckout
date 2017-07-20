package com.banas.market.checkout.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class ItemService {

    @Autowired
    private ItemDAO itemDAO;

    @Transactional
    public Optional<Item> getByBarcode(String barcode) {
        return itemDAO.getByBarcode(barcode);
    }

    @Transactional
    public Optional<Item> getItemWithDiscounts(long itemId) {
        return itemDAO.getItemWithDiscounts(itemId);
    }

    @Transactional
    public List<Item> getAllItems() {
        return itemDAO.getAll();
    }
}
