package agh.ics.oop.model.world_map;

import agh.ics.oop.model.PositionAlreadyOccupiedException;
import agh.ics.oop.model.world_elements.Grass;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class EarthMap extends AbstractWorldMap {
    public EarthMap(int width, int height) {
        super(width, height);
    }

    @Override
    public Vector2d getTranslatedPosition(Vector2d position) {
        if (position.getX() >= width)
            return new Vector2d(position.getX() % width, position.getY());
        else if(position.getX() < 0)
            return new Vector2d(width - 1, position.getY());
        return position;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() < height && position.getY() >= 0;
    }
}
