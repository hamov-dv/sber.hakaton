package com.sbt.rnd.meetup2017.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

import java.util.Arrays;

/**
 * Created by sbt-raspopov-om on 27/10/2017.
 */
final class GridConfig {
    static IgniteConfiguration igniteConfiguration(boolean clientMode) {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setNetworkTimeout(60000);
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Arrays.asList("127.0.0.1", "127.0.0.1:47500..47509"));


        discoverySpi.setIpFinder(ipFinder);
        igniteConfiguration.setDiscoverySpi(discoverySpi);
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setLocalHost("127.0.0.1");
        igniteConfiguration.setClientMode(clientMode);
        igniteConfiguration.setIgniteInstanceName("testGrid-server");
        return igniteConfiguration;
    }
}
