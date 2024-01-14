package agh.ics.oop.model;

/**
 * Enum representing possible directions of motion for the animal
 */
public enum MoveDirection {
    FORWARD("Forward"),
    BACKWARD("Backward"),
    LEFT("Left"),
    RIGHT("Right");

    private final String directionString;
    MoveDirection(String directionString){
        this.directionString = directionString;
    }

    @Override
    public String toString(){
        return directionString;
    }
}
