package com.apd.tema2;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;
import com.apd.tema2.entities.Lane;
import com.apd.tema2.io.Reader;

import java.util.ArrayList;
import java.util.Set;

public class Main {
    public static Pedestrians pedestrians = null;
    public static Intersection intersection;
    public static ArrayList<Lane> lanes;
    public static int carsNo;

    public static void main(String[] args) {
        Reader fileReader = Reader.getInstance(args[0]);
        Set<Thread> threads = fileReader.getCarsFromInput();


        if (lanes != null) {
            for (Lane lane : lanes)
                threads.add(new Thread(lane));
        }

        for (Thread car : threads) {
            car.start();
        }
        if (pedestrians != null) {
            try {
                Thread p = new Thread(pedestrians);
                p.start();
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread car : threads) {
            try {
                car.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
