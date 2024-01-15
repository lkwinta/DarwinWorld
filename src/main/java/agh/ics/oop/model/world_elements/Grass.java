package agh.ics.oop.model.world_elements;

public record Grass(Vector2d position) implements IWorldElement {

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
}
