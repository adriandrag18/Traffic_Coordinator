package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.IntersectionHandler;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.util.concurrent.BrokenBarrierException;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                    sleep(car.getWaitingTime() / 10);
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    SimpleNRoundabout intersection = (SimpleNRoundabout)Main.intersection;
                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                    intersection.roundabout.acquire();
                    System.out.println("Car " + car.getId() + " has entered the roundabout");

                    sleep(intersection.getTime() / 10);

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            intersection.getTime() / 1000 + " seconds");
                    intersection.roundabout.release();
                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    SimpleStrict1CarRoundabout intersection = (SimpleStrict1CarRoundabout)Main.intersection;
                    System.out.println("Car " + car.getId() + " has reached the roundabout");

                    synchronized (intersection.lanes[car.getStartDirection()]){
                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane " +
                                car.getStartDirection());
                        sleep(intersection.getTime() / 10);
                        System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                                intersection.getTime() / 1000 + " seconds");
                    }
                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    SimpleStrictXCarRoundabout intersection = (SimpleStrictXCarRoundabout)Main.intersection;

                    System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");
                    intersection.getArrived().await();

                    intersection.getSemaphore(car.getStartDirection()).acquire();
                    System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " +
                            car.getStartDirection());
                    intersection.getEntered().await();

                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " +
                            car.getStartDirection());
                    sleep(intersection.getTime() / 10);
                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            intersection.getTime() / 1000 + " seconds");

                    intersection.getSemaphore(car.getStartDirection()).release();
                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    SimpleMaxXCarRoundabout intersection = (SimpleMaxXCarRoundabout)Main.intersection;

                    sleep(car.getWaitingTime() / 10);

                    System.out.println("Car " + car.getId() + " has reached the roundabout from lane " +
                            car.getStartDirection());

                    intersection.getSemaphore(car.getStartDirection()).acquire();
                    System.out.println("Car " + car.getId() + " has entered the roundabout from lane " +
                            car.getStartDirection());
                    sleep(intersection.getTime() / 10);

                    System.out.println("Car " + car.getId() + " has exited the roundabout after " +
                            intersection.getTime() / 1000 + " seconds");

                    intersection.getSemaphore(car.getStartDirection()).release();
                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException {
                    PriorityIntersection intersection = (PriorityIntersection)Main.intersection;

                    sleep(car.getWaitingTime() / 10);
                    if (car.getPriority() == 1) {
                        System.out.println("Car " + car.getId() +
                                " with low priority is trying to enter the intersection...");

                        intersection.getSemaphore().acquire(intersection.getNumPriorityCars());
                        System.out.println("Car " + car.getId() + " with low priority has entered the intersection");

                        intersection.getSemaphore().release(intersection.getNumPriorityCars());
                    } else {
                        intersection.getSemaphore().acquire();
                        System.out.println("Car " + car.getId() + " with high priority has entered the intersection");

                        sleep(Constants.PRIORITY_INTERSECTION_PASSING / 10);

                        System.out.println("Car " + car.getId() + " with high priority has exited the intersection");
                        intersection.getSemaphore().release();
                    }
                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                     Crosswalk intersection = (Crosswalk) Main.intersection;
                     int semaphoreState = Constants.INITIAL;
                     while ((!Main.pedestrians.isFinished()) || semaphoreState == Constants.WAS_RED) {
                         if (!Main.pedestrians.isPass()) {
                             if (semaphoreState != Constants.WAS_GREEN) {
                                System.out.println("Car " + car.getId() + " has now green light");
                                semaphoreState = Constants.WAS_GREEN;
                             }
                         } else{
                             System.out.println("Car " + car.getId() + " has now red light");
                             semaphoreState = Constants.WAS_RED;
                             intersection.getSemaphore().acquire();
                             intersection.getSemaphore().release();
                         }
                     }
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    SimpleMaintenance intersection = (SimpleMaintenance)Main.intersection;
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has reached the bottleneck");

                    intersection.getLane(car.getStartDirection()).acquire();
                    intersection.getBarrier(car.getStartDirection()).await();

                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has passed the bottleneck");
                    intersection.getLane(car.getStartDirection() == 0 ? 1 : 0).release();
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws InterruptedException, BrokenBarrierException {
                    ComplexMaintenance intersection = (ComplexMaintenance) Main.intersection;
                    int newLaneNumber = intersection.getLane(car.getStartDirection());
                    NewLane newLane = (NewLane)Main.lanes.get(newLaneNumber);

                    synchronized (newLane) {
                        System.out.println("Car " + car.getId() + " has come from the lane number " +
                                car.getStartDirection());
                        newLane.Add(car, car.getStartDirection());
                    }
                    synchronized (car) {
                        intersection.getBarrier().await();
                        car.wait();
                    }
                    synchronized (newLane) {
                        System.out.println("Car " + car.getId() + " from the lane " + car.getStartDirection() +
                                " has entered lane number " + newLaneNumber);
                        newLane.getOrderInOldLane().release();
                    }
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) throws BrokenBarrierException, InterruptedException {
                    Railroad intersection = (Railroad) Main.intersection;
                    RailroadLane lane = (RailroadLane) Main.lanes.get(car.getStartDirection());

                    synchronized (lane) {
                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                                " has stopped by the railroad");
                        lane.Add(car);
                    }
                    intersection.getBarrier().await();
                    if (car.getId() == 0)
                        System.out.println("The train has passed, cars can now proceed");

                    synchronized (car) {
                        intersection.getBarrier().await();
                        // asteapta sa fie notificate de lane
                        car.wait();
                    }
                    System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() +
                            " has started driving");
                    lane.getSemaphore().release();
                }
            };
            default -> null;
        };
    }
}
