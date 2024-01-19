package agh.ics.oop.model.world_elements;

import javafx.scene.paint.Color;

public record Grass(Vector2d position) implements IWorldElement {
    @Override
    public Vector2d getPosition() { return position; }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getResourceName() {
        return "grass.png";
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }
}
