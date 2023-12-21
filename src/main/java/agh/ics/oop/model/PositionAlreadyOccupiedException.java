package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.Vector2d;

public class PositionAlreadyOccupiedException extends Exception {
    public PositionAlreadyOccupiedException(Vector2d position){
        super("Position " + position + "is already occupied!");
    }
}
