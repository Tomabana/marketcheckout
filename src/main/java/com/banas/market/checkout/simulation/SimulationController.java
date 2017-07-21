package com.banas.market.checkout.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationController.class);

    @Autowired
    private SimulationService simulationService;

    @RequestMapping("/{numberOfBaskets}/{maxNumberOfItemsInBasket}")
    public String runSimulation(@PathVariable(value = "numberOfBaskets") int numberOfBaskets,
                                @PathVariable(value = "maxNumberOfItemsInBasket") int maxNumberOfItemsInBasket) {
        LOGGER.info("Running simulation with param: numberOfBaskets = {}, maxNumberOfItemsInBasket = {} .",
                numberOfBaskets, maxNumberOfItemsInBasket);
        simulationService.runSimulation(numberOfBaskets, maxNumberOfItemsInBasket);
        return "success";
    }
}
