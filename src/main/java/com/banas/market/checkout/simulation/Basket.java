package com.banas.market.checkout.simulation;

import com.banas.market.checkout.inventory.Item;

import java.util.ArrayList;
import java.util.List;

class Basket {

    private List<Item> items;

    Basket() {
        this.items = new ArrayList<>();
    }

    List<Item> getItems() {
        return items;
    }
}
