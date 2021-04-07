package com.apd.tema2.factory;

import com.apd.tema2.entities.Car;

/**
 * Clasa Factory ce ajuta la crearea Thread-urilor de tip Car.
 */
public class CarFactory {

    public static Car getCar(String handlerType, String[] args) {
        return switch (args.length) {
            case 2 -> new Car(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    IntersectionHandlerFactory.getHandler(handlerType));
            case 3 -> new Car(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    IntersectionHandlerFactory.getHandler(handlerType));
            case 4 -> new Car(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    IntersectionHandlerFactory.getHandler(handlerType));
            case 5 -> new Car(Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]),
                    Integer.parseInt(args[3]),
                    IntersectionHandlerFactory.getHandler(handlerType),
                    Integer.parseInt(args[4]));
            default -> null;
        };
    }

}
