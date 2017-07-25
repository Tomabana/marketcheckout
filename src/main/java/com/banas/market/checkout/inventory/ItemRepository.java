package com.banas.market.checkout.inventory;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {

    @Query(value = "SELECT item FROM Item item LEFT JOIN FETCH item.combinedDiscounts " +
            "LEFT JOIN FETCH item.quantityDiscounts where item.id = :id")
    Item findByIdWithDiscounts(@Param("id") long id);

    Item findByBarcode(String barcode);

    List<Item> findAll();
}