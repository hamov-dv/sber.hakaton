package com.sbt.rnd.meetup2017.ignite;


import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

/**
 * Created by sbt-raspopov-om on 27/10/2017.
 */
public class RunServerNode {
    public static void main(String[] args) {
        Ignite ignite = Ignition.start(GridConfig.igniteConfiguration(false));
    }
}
