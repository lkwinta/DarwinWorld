package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_elements.Water;
import com.google.common.collect.Streams;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OceanMap extends AbstractWorldMap {
    private final ConcurrentHashMap<Vector2d, Water> waterHashMap;
    public OceanMap(int width, int height) {
        super(width, height);
        waterHashMap = new ConcurrentHashMap<>();
    }

    @Override
    public Vector2d getTranslatedPosition(Vector2d position) {
        return position;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return this.getMapBoundary().inBounds(position) && !waterHashMap.containsKey(position);
    }

    @Override
    public void place(IWorldElement element) {
        if(element instanceof Water water){
            waterHashMap.put(water.getPosition(), water);
            mapChanged("Water placed at: " + water.getPosition());
        } else {
            super.place(element);
        }
    }

    @Override
    public void remove(IWorldElement element) {
        if(element instanceof Water water){
            waterHashMap.remove(water.getPosition());
            mapChanged("Water removed from: " + water.getPosition());
        } else {
            super.remove(element);
        }
    }

    @Override
    public Stream<IWorldElement> objectsAt(Vector2d position) {
        return Streams.concat(
            waterHashMap.containsKey(position) ? Stream.of(waterHashMap.get(position)) : Stream.empty(), //put water first in stream
            super.objectsAt(position)
        );
    }

    @Override
    public List<IWorldElement> getElements() {
        return Streams.concat(
            waterHashMap.values().stream(),
            super.getElements().stream()
        ).collect(Collectors.toList());
    }
}
