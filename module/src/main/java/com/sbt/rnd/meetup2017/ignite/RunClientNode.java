package com.sbt.rnd.meetup2017.ignite;

import com.sbt.rnd.meetup2017.data.ogm.Client;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;

/**
 * Created by sbt-raspopov-om on 01/11/2017.
 */
public class RunClientNode {
    public static void main(String[] args) {
        try(Ignite ignite = Ignition.start(GridConfig.igniteConfiguration(true))){
            try(IgniteCache<Long, Client> clientIgniteCache = ignite.getOrCreateCache(CacheConfig.cacheConfiguration())){
                Client client = new Client("Ivan", "616123456789");
                clientIgniteCache.put(client.getId(), client);
                System.out.println("get Client "+clientIgniteCache.get(client.getId()));
            }
        }
    }
}
