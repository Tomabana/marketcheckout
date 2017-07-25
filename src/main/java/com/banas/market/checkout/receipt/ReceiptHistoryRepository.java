package com.banas.market.checkout.receipt;

import com.banas.market.checkout.receipt.entities.ReceiptHistory;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptHistoryRepository extends CrudRepository<ReceiptHistory, Long> {
}