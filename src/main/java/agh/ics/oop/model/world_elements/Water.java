package agh.ics.oop.model.world_elements;

import javafx.scene.paint.Color;

public record Water(Vector2d position) implements IWorldElement {
    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public String toString() {
        return "#";
    }

    @Override
    public String getResourceName() {
        return "water.png";
    }

    @Override
    public Color getColor() {
        return Color.BLUE;
    }
}
