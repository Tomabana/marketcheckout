package com.banas.market.checkout.simulation;

import com.banas.market.checkout.Checkout;
import com.banas.market.checkout.receipt.ReceiptRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulationTest {

    private static final int NUMBER_OF_BASKETS = 10;
    private static final int NUMBER_OF_CHECKOUTS = 3;
    private static final int MAX_NUMBER_OF_ITEMS_IN_BASKET = 10;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Test
    public void runSimulation() throws InterruptedException {
        simulationService.runSimulation(NUMBER_OF_BASKETS, MAX_NUMBER_OF_ITEMS_IN_BASKET);

        Assert.assertEquals(receiptRepository.count(), NUMBER_OF_BASKETS);
        receiptRepository.findAll().forEach(receipt ->
                Assert.assertTrue(receipt.getTotalPriceIncludingDiscounts().compareTo(BigDecimal.ZERO) > 0));
    }

    @Configuration
    @EnableAsync
    @ComponentScan("com.banas.market.checkout")
    static class Config {

        @Bean
        List<Checkout> checkoutsList() {
            List<Checkout> checkouts = new ArrayList<>();
            for (int i = 0; i < NUMBER_OF_CHECKOUTS; i++) {
                checkouts.add(checkout());
            }
            return checkouts;
        }

        @Bean
        @Scope("prototype")
        Checkout checkout() {
            return new Checkout();
        }
    }
}