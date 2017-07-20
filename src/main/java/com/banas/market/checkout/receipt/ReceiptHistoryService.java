package com.banas.market.checkout.receipt;

import com.banas.market.checkout.receipt.entities.ReceiptHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Component
@Service
public class ReceiptHistoryService {

    @Autowired
    private ReceiptHistoryDAO receiptHistoryDAO;

    @Transactional
    public void save(Receipt receipt) {
        receiptHistoryDAO.save(new ReceiptHistory(receipt));
    }
}
