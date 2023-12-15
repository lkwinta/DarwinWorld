package agh.ics.oop.model.world_elements;

import agh.ics.oop.model.IMoveValidator;
import agh.ics.oop.model.MapDirection;
import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.Vector2d;

public class Animal implements IWorldElement {
    private Vector2d position;
    private MapDirection orientation;
    private int energyLevel;

    @Override
    public Vector2d getPosition() {
        return null;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return false;
    }

    public void move(MoveDirection direction, IMoveValidator moveValidator){

    }

    public MapDirection getOrientation() {
        return orientation;
    }
}
