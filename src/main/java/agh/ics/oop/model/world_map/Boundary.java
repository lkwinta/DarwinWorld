package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

public record Boundary(Vector2d bottomLeft, Vector2d topRight) {
    public int getArea() {
        return (topRight.x() - bottomLeft.x()) * (topRight.y() - bottomLeft.y());
    }
}
