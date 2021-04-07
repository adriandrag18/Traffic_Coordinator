package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {
    private final int time;
    public final Semaphore roundabout;

    public SimpleNRoundabout(int maxNumCars, int time) {
        this.time = time;
        roundabout = new Semaphore(maxNumCars);
    }

    public int getTime() {
        return time;
    }
}
