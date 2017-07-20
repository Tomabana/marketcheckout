package com.banas.market.checkout.inventory;

import com.banas.market.checkout.HibernateDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Component
@Repository
class ItemDAO extends HibernateDAOImpl<Item> {

    @Autowired
    public ItemDAO(EntityManager entityManager) {
        super(Item.class, entityManager);
    }

    Optional<Item> getByBarcode(String barcode) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Item> criteriaQuery = getCriteria();
        Root<Item> itemRoot = criteriaQuery.from(Item.class);
        criteriaQuery.select(itemRoot);
        criteriaQuery.where(criteriaBuilder.equal(itemRoot.get(Item_.barcode), barcode));
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    Optional<Item> getItemWithDiscounts(long itemId) {
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        CriteriaQuery<Item> criteriaQuery = getCriteria();
        Root<Item> itemRoot = criteriaQuery.from(Item.class);
        itemRoot.fetch(Item_.quantityDiscounts, JoinType.LEFT);
        itemRoot.fetch(Item_.combinedDiscounts, JoinType.LEFT);
        criteriaQuery.where(criteriaBuilder.equal(itemRoot.get(Item_.id), itemId));
        criteriaQuery.distinct(true);
        return entityManager.createQuery(criteriaQuery).getResultList().stream().findFirst();
    }

    List<Item> getAll() {
        CriteriaQuery<Item> criteriaQuery = getCriteria();
        Root<Item> itemRoot = criteriaQuery.from(Item.class);
        criteriaQuery.select(itemRoot);

        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
