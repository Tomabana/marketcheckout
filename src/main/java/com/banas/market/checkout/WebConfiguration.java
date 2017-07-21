package com.banas.market.checkout;

import com.banas.market.checkout.simulation.SimulationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@SpringBootApplication
public class WebConfiguration {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebConfiguration.class, args);
        context.getBean(SimulationService.class).runSimulation(10, 10);
    }

    @Bean
    public List<Checkout> checkoutsList() {
        List<Checkout> checkouts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            checkouts.add(checkout());
        }
        return checkouts;
    }

    @Bean
    @Scope("prototype")
    public Checkout checkout() {
        return new Checkout();
    }
}
