package agh.ics.oop.model.world_elements;

import agh.ics.oop.model.Vector2d;

public interface IWorldElement {
    Vector2d getPosition();

    boolean isAt(Vector2d position);

    @Override
    String toString();
}
