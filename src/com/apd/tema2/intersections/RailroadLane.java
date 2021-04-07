package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Lane;

import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class RailroadLane implements Lane {
    private final LinkedBlockingQueue<Car> cars;
    private final Semaphore semaphore;

    public RailroadLane() {
        cars = new LinkedBlockingQueue<>();
        semaphore = new Semaphore(0);
    }

    @Override
    public void run() {
        Railroad intersection = (Railroad) Main.intersection;
        try {
            // asteapta ca toate masinile sa ajunga la intersectie
            intersection.getBarrier().await();
            // asteapta ca trenul sa treaca si ca thread urile din Car sa de synchronized pe car
            intersection.getBarrier().await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (!cars.isEmpty()) {
            Car car = Objects.requireNonNull(cars.poll());
            synchronized (car) {
                car.notify();
            }
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void Add(Car car) {
        cars.add(car);
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }
}
