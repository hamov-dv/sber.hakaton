package com.sbt.rnd.meetup2017.dao;

import javax.persistence.EntityManager;

public interface IExecutor<T> {
    T execute(EntityManager em) throws Exception;
}
