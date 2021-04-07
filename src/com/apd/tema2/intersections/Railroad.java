package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Railroad implements Intersection {
    private final CyclicBarrier barrier;

    public Railroad() {
        barrier = new CyclicBarrier(Main.carsNo + 2);
        Main.lanes = new ArrayList<>();
        Main.lanes.add(new RailroadLane());
        Main.lanes.add(new RailroadLane());
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }
}
