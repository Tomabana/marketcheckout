package com.banas.market.checkout.simulation;

import com.banas.market.checkout.receipt.ReceiptRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimulationTest {

    private static final int NUMBER_OF_BASKETS = 10;
    private static final int MAX_NUMBER_OF_ITEMS_IN_BASKET = 10;

    @Autowired
    private SimulationService simulationService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Test
    public void simulation() {
        simulationService.runSimulation(NUMBER_OF_BASKETS, MAX_NUMBER_OF_ITEMS_IN_BASKET);

        Assert.assertEquals(receiptRepository.count(), NUMBER_OF_BASKETS);
    }

}
