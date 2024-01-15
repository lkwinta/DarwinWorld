package agh.ics.oop.model.world_elements;

@SuppressWarnings("unused")
public interface IWorldElement {
    Vector2d position();

    boolean isAt(Vector2d position);

    @Override
    String toString();

    String getResourceName();
}
