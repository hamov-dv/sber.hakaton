package com.sbt.rnd.meetup2017.config;

import com.sbt.rnd.meetup2017.dao.Dao;
import com.sbt.rnd.meetup2017.dao.IDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.EntityManager;

@Configuration
@DependsOn(value = {"entityManager"})
public class DaoConfig {

    @Bean
    IDao dao(EntityManager em){
        return new Dao(em);
    }
}
