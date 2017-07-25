package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.inventory.Item;
import com.banas.market.checkout.inventory.ItemRepository;
import com.banas.market.checkout.payment.CashPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Service
public class SimulationService {

    @Autowired
    private CashPaymentService cashPaymentService;

    @Autowired
    private ItemRepository itemRepository;

    @Resource(name = "checkoutsList")
    private List<Checkout> checkouts;

    public void runSimulation(int numberOfBaskets, int maxNumberOfItemsInBasket) {
        ArrayBlockingQueue<Basket> basketsQueue = createBasketsQueue(numberOfBaskets, maxNumberOfItemsInBasket);
        ExecutorService executorService = Executors.newFixedThreadPool(checkouts.size());
        for (Checkout checkout : checkouts) {
            executorService.execute(new CheckoutRunner(checkout, basketsQueue, cashPaymentService));
        }
        executorService.shutdown();
    }

    private ArrayBlockingQueue<Basket> createBasketsQueue(int numberOfBaskets, int maxNumberOfItemsInBasket) {
        List<Item> allAvailableItems = itemRepository.findAll();
        Assert.notEmpty(allAvailableItems, "No items in database. Simulation cannot be run.");
        ArrayBlockingQueue<Basket> basketsQueue = new ArrayBlockingQueue<>(numberOfBaskets);
        for (int i = 0; i < numberOfBaskets; i++) {
            Basket basket = new Basket();
            basketsQueue.add(basket);
            int numberOfItemsInBasket = ThreadLocalRandom.current().nextInt(1, maxNumberOfItemsInBasket + 1);
            for (int j = 0; j < numberOfItemsInBasket; j++) {
                int randomItemIndex = ThreadLocalRandom.current().nextInt(0, allAvailableItems.size());
                basket.getItems().add(allAvailableItems.get(randomItemIndex));
            }
        }
        return basketsQueue;
    }


}
