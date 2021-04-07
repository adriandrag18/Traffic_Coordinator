package com.apd.tema2.entities;

import com.apd.tema2.Main;
import com.apd.tema2.intersections.Crosswalk;
import com.apd.tema2.utils.Constants;

import static java.lang.Thread.sleep;

/**
 * Clasa utilizata pentru gestionarea oamenilor care se strang la trecerea de pietoni.
 */
public class Pedestrians implements Runnable {
    private int pedestriansNo = 0;
    private final int maxPedestriansNo;
    private boolean pass = false;
    private volatile boolean finished = false;
    private final int executeTime;
    private final long startTime;

    public Pedestrians(int executeTime, int maxPedestriansNo) {
        this.startTime = System.currentTimeMillis();
        this.executeTime = executeTime;
        this.maxPedestriansNo = maxPedestriansNo;
    }

    @Override
    public void run() {
        while(System.currentTimeMillis() - startTime < executeTime / 10) {
            try {
                pedestriansNo++;
                sleep(Constants.PEDESTRIAN_COUNTER_TIME / 10);

                if(pedestriansNo == maxPedestriansNo) {
                    pedestriansNo = 0;
                    ((Crosswalk)Main.intersection).getSemaphore().acquire();
                    pass = true;
                    sleep(Constants.PEDESTRIAN_PASSING_TIME / 10);
                    pass = false;
                    ((Crosswalk)Main.intersection).getSemaphore().release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        finished = true;
    }

    public boolean isPass() {
        return pass;
    }

    public boolean isFinished() {
        return finished;
    }
}
