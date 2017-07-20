package com.banas.market.checkout.receipt;

import com.banas.market.checkout.HibernateDAOImpl;
import com.banas.market.checkout.receipt.entities.ReceiptHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Component
@Repository
class ReceiptHistoryDAO extends HibernateDAOImpl<ReceiptHistory> {

    @Autowired
    ReceiptHistoryDAO(EntityManager entityManager) {
        super(ReceiptHistory.class, entityManager);
    }
}
