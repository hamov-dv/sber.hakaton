package com.sbt.rnd.meetup2017.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.sbt.rnd.meetup2017.Environment.PERSISTENCE_UNIT_NAME;

@Configuration
public class EntityManagerConfig {

    @Bean
    EntityManager entityManager(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        return emf.createEntityManager();
    }
}
