package agh.ics.oop.model.world_map;

import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.Grass;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import com.google.common.collect.Streams;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public abstract class AbstractWorldMap implements IMoveHandler {
    protected final MapVisualizer mapVisualizer;
    protected final ConcurrentMap<Vector2d, List<Animal>> animalsMap;
    protected final ConcurrentMap<Vector2d, Grass> grassMap;
    private final List<IMapChangeListener> listeners;
    @Getter
    private final UUID mapId;
    @Getter
    private final Boundary mapBoundary;
    protected final int width;
    protected final int height;

    protected AbstractWorldMap(int width, int height){
        this.animalsMap = new ConcurrentHashMap<>();
        this.grassMap = new ConcurrentHashMap<>();

        this.mapVisualizer = new MapVisualizer(this);
        this.listeners = new ArrayList<>();

        this.mapId = UUID.randomUUID();
        this.mapBoundary = new Boundary(
                new Vector2d(0,0),
                new Vector2d(width - 1, height - 1)
        );

        this.width = width;
        this.height = height;
    }

    public void addListener(IMapChangeListener listener){
        this.listeners.add(listener);
    }
    public void removeListener(IMapChangeListener listener) {
        this.listeners.remove(listener);
    }

    protected void mapChanged(String message){
        listeners.forEach((listener) -> listener.mapChanged(this, message));
    }

    protected <T extends IWorldElement> void addToHashMap(Map<Vector2d, List<T>> map, Vector2d position, T element){
        if(!map.containsKey(position))
            map.put(position, new ArrayList<>());
        map.get(position).add(element);
    }

    protected <T extends IWorldElement> void removeFromHashMap(Map<Vector2d, List<T>> map, Vector2d position, T element){
        if(map.containsKey(position))
            map.get(position).remove(element);
        if(map.get(position).isEmpty())
            map.remove(position);
    }

    public void place(IWorldElement object) {
        if(!this.getMapBoundary().inBounds(object.getPosition()))
            throw new IllegalArgumentException("Cannot place object outside of map bounds");

        if(object instanceof Animal animal) {
            addToHashMap(animalsMap, animal.getPosition(), animal);
            mapChanged("Animal placed at: " + animal.getPosition());
        } else if (object instanceof Grass grass) {
            grassMap.put(grass.getPosition(), grass);
            mapChanged("Grass placed at: " + grass.getPosition());
        }
    }

    public void remove(IWorldElement object) {
        if(object instanceof Animal animal) {
            removeFromHashMap(animalsMap, animal.getPosition(), animal);
            mapChanged("Animal removed from: " + animal.getPosition());
        } else if (object instanceof Grass grass) {
            grassMap.remove(grass.getPosition());
            mapChanged("Grass removed from: " + grass.getPosition());
        }
    }

    public void move(IWorldElement object) throws IllegalArgumentException {
        if(!(object instanceof Animal animal))
            throw new IllegalArgumentException("You can't move object that is not an Animal'");

        Vector2d oldPosition = animal.getPosition();

        animal.move( this);

        if(!animal.getPosition().equals(oldPosition)){
            removeFromHashMap(animalsMap, oldPosition, animal);
            addToHashMap(animalsMap, animal.getPosition(), animal);
            mapChanged("Animal at %s moved to: %s with orientation: %s".formatted(
                    oldPosition,
                    animal.getPosition(),
                    animal.getOrientation()
            ));
        }
        else
            mapChanged("Cannot move animal at: %s".formatted(animal.getPosition()));
    }

    public boolean isOccupied(Vector2d position) {
        return objectsAt(position).findAny().isPresent();
    }

    public Stream<IWorldElement> objectsAt(Vector2d position) {
        return Streams.concat(
            animalsMap.containsKey(position) ? animalsMap.get(position).stream() : Stream.empty(),
            grassMap.containsKey(position) ? Stream.of(grassMap.get(position)) : Stream.empty()
        );
    }

    public List<IWorldElement> getElements() {
        return Streams.concat(
            grassMap.values().stream().map(IWorldElement.class::cast),
            animalsMap.values().stream().flatMap(Collection::stream).toList().stream().map(IWorldElement.class::cast)
        ).toList();
    }

    public String toString() {
        Boundary currentBounds = getMapBoundary();
        return mapVisualizer.draw(currentBounds.bottomLeft(), currentBounds.topRight());
    }
}
