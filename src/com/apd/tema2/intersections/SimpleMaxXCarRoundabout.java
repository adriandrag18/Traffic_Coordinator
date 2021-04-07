package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleMaxXCarRoundabout implements Intersection {
    private final int time;
    private final Semaphore[] semaphoreLanes;

    public SimpleMaxXCarRoundabout(int numLanes, int time, int numCars) {
        this.time = time;
        semaphoreLanes = new Semaphore[numLanes];
        for (int i = 0; i < numLanes; i++)
            semaphoreLanes[i] = new Semaphore(numCars);

    }

    public int getTime() {
        return time;
    }

    public Semaphore getSemaphore(int lane) {
        return semaphoreLanes[lane];
    }
}
