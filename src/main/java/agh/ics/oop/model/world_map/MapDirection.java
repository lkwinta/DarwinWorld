package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

@SuppressWarnings("unused")
public enum MapDirection {
    NORTH("North", new Vector2d(0, 1)),
    NORTH_EAST("NorthEast", new Vector2d(1, 1)),
    EAST("East", new Vector2d(1, 0)),
    SOUTH_EAST("SouthEast", new Vector2d(1, -1)),
    SOUTH("South", new Vector2d(0, -1)),
    SOUTH_WEST("SouthWest", new Vector2d(-1, -1)),
    WEST("West", new Vector2d(-1, 0)),
    NORTH_WEST("NorthWest", new Vector2d(-1, 1));

    private final String stringRepresentation;
    private final Vector2d vectorRepresentation;

    MapDirection(String stringRepresentation, Vector2d vectorRepresentation){
        this.stringRepresentation = stringRepresentation;
        this.vectorRepresentation = vectorRepresentation;
    }

    @Override
    public String toString(){
        return stringRepresentation;
    }

    public MapDirection next(){
        return values()[(this.ordinal() + 1) % values().length];
    }

    public MapDirection previous() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    public Vector2d toUnitVector() {
        return vectorRepresentation;
    }

    public MapDirection shift(int value) {
        return values()[(this.ordinal() + value) % values().length];
    }
}
