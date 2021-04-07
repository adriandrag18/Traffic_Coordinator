package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Lane;

import java.util.ArrayList;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class NewLane implements Lane {
    private final int start;
    private final int end;
    private final TreeMap<Integer, LinkedBlockingQueue<Car>> lanes;
    private final Semaphore orderInOldLane;

    public NewLane(int start, int end) {
        this.start = start;
        this.end = end;
        lanes = new TreeMap<>();
        for (int i = start; i < end; i ++)
            lanes.put(i, new LinkedBlockingQueue<>());
        orderInOldLane = new Semaphore(0);
    }

    public void Add(Car car, int oldLane) {
        lanes.get(oldLane).add(car);
    }

    public Semaphore getOrderInOldLane() {
        return orderInOldLane;
    }

    @Override
    public void run() {
        int index = 0, passedCars = 0;
        int maxNumberCars = ((ComplexMaintenance) Main.intersection).getNumPassCars();
        ArrayList<Integer> lanesInQueue = new ArrayList<>(); // indicii benzilor vechi care au ramas in coada
        for (int i = start; i < end; i++)
            lanesInQueue.add(i);

        try {
            // asteapta ca toate masinile sa ajunga de pe toate benzile
            ((ComplexMaintenance) Main.intersection).getBarrier().await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }

        while (!lanes.isEmpty()) {
            int currentLane = lanesInQueue.get(index);
            while (!lanes.get(currentLane).isEmpty() && passedCars < maxNumberCars) {
                Car car = Objects.requireNonNull(lanes.get(currentLane).poll());
                synchronized (car) {
                    car.notify();
                }
                try {
                    // nu lasa threadul sa notifice urmatoarea masina pana masina a afisat mesajul
                    orderInOldLane.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passedCars++;
            }
            if (lanes.get(currentLane).isEmpty()) {
                System.out.println("The initial lane " + currentLane +
                        " has been emptied and removed from the new lane queue");
                lanes.remove(currentLane);
                lanesInQueue.remove((Object)currentLane);
                index--;
            } else if (passedCars == maxNumberCars)
                System.out.println("The initial lane " + currentLane +
                        " has no permits and is moved to the back of the new lane queue");

            index++;
            if (index >= lanes.size())
                index = 0;
            passedCars = 0;
        }
    }
}