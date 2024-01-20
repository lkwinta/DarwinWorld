package agh.ics.oop.model.world_elements;

import javafx.scene.paint.Color;

@SuppressWarnings("unused")
public interface IWorldElement {
    Vector2d getPosition();

    boolean isAt(Vector2d position);

    @Override
    String toString();

    String getResourceName();

    Color getColor();
}
