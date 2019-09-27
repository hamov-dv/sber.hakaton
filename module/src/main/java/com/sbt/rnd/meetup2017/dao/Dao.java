package com.sbt.rnd.meetup2017.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Map;

public class Dao implements IDao {

    private EntityManager entityManager;

    private FullTextEntityManager fullTextEntityManager;

    private static final Logger LOGGER = LogManager.getLogger(Dao.class.getName());

    public Dao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    }

    @Override
    public FullTextEntityManager getFullTextEntityManager() {
        return fullTextEntityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public synchronized <T> T execute(IExecutor<T> executor) {
        boolean inTransaction = entityManager.getTransaction().isActive();
        int txId = entityManager.getTransaction().hashCode();
        try {
            if (!inTransaction) {
                entityManager.getTransaction().begin();
                LOGGER.trace("Tx start {}, {}, thread = {}:{}",
                        txId, executor.getClass().getName(), Thread.currentThread().getId(), Thread.currentThread().getName());
            }
            T res = executor.execute(entityManager);

            if (!inTransaction) {
                entityManager.getTransaction().commit();
                LOGGER.trace("Tx commit {}, {}, thread = {}:{}",
                        txId, executor.getClass().getName(), Thread.currentThread().getId(), Thread.currentThread().getName());
            }
            return res;

        } catch (Exception ex) {
            LOGGER.error("Tx fail id = " + txId + ", " + executor.getClass().getName(), ex);
            try {

                if (!inTransaction) {
                    if (entityManager.getTransaction().isActive())
                        entityManager.getTransaction().rollback();
                }

            } catch (Exception e) {
                LOGGER.error("Ошибка при фиксации транзакции {}. Cause: ",
                        txId, e.getCause() != null ? e.getCause().getMessage() : e.getMessage(), e);
                throw e;
            }

        }
        return null;
    }

    public synchronized <T> Boolean save(T entity) {
        return save(entity,false);
    }

    public synchronized <T> Boolean save(T entity,Boolean lock) {
        return execute(new IExecutor<Boolean>() {
            @Override
            public Boolean execute(EntityManager em) throws Exception {
                if (lock)
                    em.lock(entity, LockModeType.OPTIMISTIC);
                em.persist(entity);

                return true;
            }
        });
    }

    public synchronized <T> Boolean remove(T entity) {
        return execute(new IExecutor<Boolean>() {
            @Override
            public Boolean execute(EntityManager em) throws Exception {
                em.remove(entity);
                return true;
            }
        });
    }

    public synchronized <T> T findById(Class<T> entityClass, Long id) {
        if (id==null)
            throw new RuntimeException("Ошибка при чтении сущности "+entityClass+": id не может быть пустым.");
        return entityManager.find(entityClass, id);
    }

    public synchronized <T> List<T> search(String query) {

        return search(query, null);
    }

    public synchronized <T> List<T> search(String query, Map<String, Object> parameters) {
        javax.persistence.Query q = entityManager.createQuery(query);
        if (parameters != null && parameters.size() > 0)
            for (Map.Entry<String, Object> entry : parameters.entrySet())
                q.setParameter(entry.getKey(), entry.getValue());
        return q.getResultList();
    }

    public synchronized <T> List<T> fullTextSearch(Class<T> entityClass, Query query) {

        //Optionally use the QueryBuilder to simplify Query definition:

        //Create a Lucene Query:
        //Query lq = b.keyword().onField("name").matching("dina").createQuery();

        //Transform the Lucene Query in a JPA Query:
        FullTextQuery ftQuery = fullTextEntityManager.createFullTextQuery(query, entityClass);

        //List all matching Hypothesis:
        return ftQuery.getResultList();
    }
}
