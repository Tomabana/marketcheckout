package com.banas.market.checkout;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class HibernateDAOImpl<E> {

    private Class<E> entityClass;

    protected EntityManager entityManager;

    public HibernateDAOImpl(Class<E> entityClass, EntityManager entityManager) {
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return entityManager.getCriteriaBuilder();
    }

    protected CriteriaQuery<E> getCriteria() {
        return entityManager.getCriteriaBuilder().createQuery(entityClass);
    }

    public E save(E e) {
        entityManager.persist(e);
        return e;
    }
}
