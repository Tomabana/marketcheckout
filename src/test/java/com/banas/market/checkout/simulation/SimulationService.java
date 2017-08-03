package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.Scanner;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Service
class SimulationService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private Scanner scanner;

    @Resource(name = "checkoutsList")
    private List<Checkout> checkouts;

    void runSimulation(int numberOfBaskets, int maxNumberOfItemsInBasket) throws InterruptedException {
        ArrayBlockingQueue<List<Item>> basketsQueue = createBasketsQueue(numberOfBaskets, maxNumberOfItemsInBasket);
        ExecutorService executorService = Executors.newFixedThreadPool(checkouts.size());
        List<Callable<Object>> tasks = new ArrayList<>();
        checkouts.forEach(checkout -> tasks.add(Executors.callable(new CheckoutRunner(checkout,
                scanner, basketsQueue))));
        executorService.invokeAll(tasks);
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
