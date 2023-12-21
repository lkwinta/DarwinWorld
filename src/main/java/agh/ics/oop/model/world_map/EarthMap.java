package agh.ics.oop.model.world_map;

import agh.ics.oop.model.PositionAlreadyOccupiedException;
import agh.ics.oop.model.world_elements.Grass;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

public class EarthMap extends AbstractWorldMap {
    private final int width;
    private final int height;
    private final Boundary mapBoundary;

    private final HashMap<Vector2d, Grass> grassMap;

    public EarthMap(int width, int height) {
        this.width = width;
        this.height = height;

        this.mapBoundary = new Boundary(new Vector2d(0,0), new Vector2d(width, height));

        grassMap = new HashMap<>();
    }

    @Override
    public Vector2d getTranslatedPosition(Vector2d position) {
        if (position.getX() >= width)
            return new Vector2d(position.getX() % width, position.getY());
        return position;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if(position.getY() >= height || position.getY() < 0)
            return false;
        return super.canMoveTo(position);
    }

    @Override
    public void place(IWorldElement object) throws PositionAlreadyOccupiedException {
        super.place(object);

        if (object instanceof Grass grass) {
            grassMap.put(grass.getPosition(), grass);
            super.mapChanged("Grass placed at: " + grass.getPosition());
        }
    }

    @Override
    public IWorldElement objectAt(Vector2d position) {
        if (animalsMap.get(position) != null)
            return super.objectAt(position);

        return grassMap.get(position);
    }

    @Override
    public List<IWorldElement> getElements() {
        return new ArrayList<>(
                Stream.concat(
                        grassMap.values().stream(),
                        super.getElements().stream()
                ).toList());
    }

    @Override
    public Boundary getCurrentBounds() {
        return mapBoundary;
    }
}
