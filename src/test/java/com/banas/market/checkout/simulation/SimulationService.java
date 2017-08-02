package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.Scanner;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Service
class SimulationService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Scanner scanner;

    @Autowired
    private Checkout checkout;

    void runSimulation(int numberOfBaskets, int maxNumberOfItemsInBasket) {
        ArrayBlockingQueue<List<Item>> basketsQueue = createBasketsQueue(numberOfBaskets, maxNumberOfItemsInBasket);
        new CheckoutRunner(checkout, scanner, basketsQueue).run();
    }

    private ArrayBlockingQueue<List<Item>> createBasketsQueue(int numberOfBaskets, int maxNumberOfItemsInBasket) {
        List<Item> allAvailableItems = itemRepository.findAll();
        if (allAvailableItems.isEmpty()) {
            throw new IllegalArgumentException("No items in database. Simulation cannot be run.");
        }
        ArrayBlockingQueue<List<Item>> basketsQueue = new ArrayBlockingQueue<>(numberOfBaskets);
        for (int i = 0; i < numberOfBaskets; i++) {
            List<Item> basket = new ArrayList<>();
            basketsQueue.add(basket);
            int numberOfItemsInBasket = ThreadLocalRandom.current().nextInt(1, maxNumberOfItemsInBasket + 1);
            for (int j = 0; j < numberOfItemsInBasket; j++) {
                int randomItemIndex = ThreadLocalRandom.current().nextInt(0, allAvailableItems.size());
                basket.add(allAvailableItems.get(randomItemIndex));
            }
        }
        return basketsQueue;
    }
}
