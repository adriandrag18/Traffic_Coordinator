package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class PriorityIntersection implements Intersection {
    private final int numPriorityCars;
    private final Semaphore semaphore;

    public PriorityIntersection(int numPriorityCars) {
        this.numPriorityCars = numPriorityCars;
        semaphore = new Semaphore(numPriorityCars);
    }

    public int getNumPriorityCars() {
        return numPriorityCars;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
