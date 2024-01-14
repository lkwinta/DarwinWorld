package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

public class OceanMap extends AbstractWorldMap {
    public OceanMap(int width, int height) {
        super(width, height);
    }

    @Override
    public Vector2d getTranslatedPosition(Vector2d position) {
        return null;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return false;
    }
}
