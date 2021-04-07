package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleMaintenance implements Intersection {
    private final Semaphore[] lanes;
    private final CyclicBarrier[] barriers;

    public SimpleMaintenance(int numPassCars) {
        lanes = new Semaphore[]{
                new Semaphore(numPassCars),
                new Semaphore(0)
        };
        barriers = new CyclicBarrier[] {
                new CyclicBarrier(numPassCars),
                new CyclicBarrier(numPassCars)
        };
    }

    public Semaphore getLane(int lane) {
        return lanes[lane];
    }

    public CyclicBarrier getBarrier(int lane) {
        return barriers[lane];
    }
}
