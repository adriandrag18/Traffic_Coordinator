package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

public class SimpleStrict1CarRoundabout implements Intersection {
    public final Object[] lanes;
    private final int time;

    public SimpleStrict1CarRoundabout(int numLanes, int time){
        lanes = new Object[numLanes];
        this.time = time;
        for (int i = 0; i < numLanes; i++)
            lanes[i] = i;
    }

    public int getTime() {
        return time;
    }
}
