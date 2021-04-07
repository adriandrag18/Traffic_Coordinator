package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class Crosswalk implements Intersection {
    private final Semaphore semaphore = new Semaphore(1);

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
