package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrictXCarRoundabout implements Intersection {
    private final int time;
    private final Semaphore[] semaphoreLanes;
    private final CyclicBarrier entered, arrived;

    public SimpleStrictXCarRoundabout(int numLanes, int time, int numCars, int totalCars) {
        this.time = time;
        semaphoreLanes = new Semaphore[numLanes];
        for (int i = 0; i < numLanes; i++)
            semaphoreLanes[i] = new Semaphore(numCars);
        entered = new CyclicBarrier(numCars * numLanes);
        arrived = new CyclicBarrier(totalCars);
    }

    public int getTime() {
        return time;
    }

    public Semaphore getSemaphore(int lane) {
        return semaphoreLanes[lane];
    }

    public CyclicBarrier getEntered() {
        return entered;
    }

    public CyclicBarrier getArrived() {
        return arrived;
    }
}
