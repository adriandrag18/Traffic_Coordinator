package com.apd.tema2.entities;

import java.util.concurrent.BrokenBarrierException;

/**
 * Utilizata pentru uniformizarea implementarii task-urilor.
 */
public interface IntersectionHandler {
    public void handle(Car car) throws InterruptedException, BrokenBarrierException;
}
