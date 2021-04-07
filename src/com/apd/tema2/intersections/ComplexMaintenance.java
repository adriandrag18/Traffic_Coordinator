package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class ComplexMaintenance implements Intersection {
    private final int numPassCars;
    private final int[] intervals;
    private final CyclicBarrier barrier;

    public ComplexMaintenance(int newLanes, int oldLanes, int numPassCars) {
        this.numPassCars = numPassCars;

        intervals = new int[newLanes + 1];
        for (int i = 0; i < newLanes + 1; i++)
            intervals[i] = (int)(i * (double)oldLanes / newLanes);

        barrier = new CyclicBarrier(Main.carsNo + newLanes);

        Main.lanes =  new ArrayList<>();
        for (int i = 0; i < newLanes; i++)
            Main.lanes.add(new NewLane(intervals[i], intervals[i + 1]));
    }

    public int getLane(int oldLane) {
        for (int i = 1; i < intervals.length; i++)
            if (intervals[i] > oldLane)
                return i - 1;
        return -1;
    }

    public int getNumPassCars() {
        return numPassCars;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }
}
