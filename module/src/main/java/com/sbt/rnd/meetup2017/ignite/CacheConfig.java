package com.sbt.rnd.meetup2017.ignite;

import com.sbt.rnd.meetup2017.data.ogm.Client;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.configuration.CacheConfiguration;

/**
 * Created by sbt-raspopov-om on 01/11/2017.
 */
final class CacheConfig {
    private static final String CLIENT_CACHE_NAME = Client.class.getSimpleName();

    public static CacheConfiguration<Long, Client> cacheConfiguration(){
        CacheConfiguration<Long, Client> cfg = new CacheConfiguration<>();
        cfg.setCacheMode(CacheMode.PARTITIONED);
        cfg.setName(CLIENT_CACHE_NAME);

        return cfg;
    }
}
