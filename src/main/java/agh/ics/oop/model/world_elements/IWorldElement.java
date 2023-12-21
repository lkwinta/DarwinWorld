package agh.ics.oop.model.world_elements;

public interface IWorldElement {
    Vector2d getPosition();

    boolean isAt(Vector2d position);

    @Override
    String toString();
}
