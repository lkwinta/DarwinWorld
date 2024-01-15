package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

public class EarthMap extends AbstractWorldMap {
    public EarthMap(int width, int height) {
        super(width, height);
    }

    @Override
    public Vector2d getTranslatedPosition(Vector2d position) {
        if (position.x() >= width)
            return new Vector2d(position.x() % width, position.y());
        else if(position.x() < 0)
            return new Vector2d(width - 1, position.y());
        return position;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.y() < height && position.y() >= 0;
    }
}
