package com.sbt.rnd.meetup2017.dao;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public interface IDao {

    FullTextEntityManager getFullTextEntityManager();

    EntityManager getEntityManager();

    <T> T execute(IExecutor<T> executor);

    <T> Boolean save(T entity);

    <T> Boolean save(T entity,Boolean lock);

    <T> Boolean remove(T entity);

    <T> T findById(Class<T> entityClass, Long id);

    <T> List<T> search(String query);

    <T> List<T> search(String query,Map<String,Object> parameters);

    <T> List<T> fullTextSearch(Class<T> entityClass, Query query);

}
