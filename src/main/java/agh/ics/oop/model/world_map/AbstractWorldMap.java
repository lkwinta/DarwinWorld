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
        animalsMap = new ConcurrentHashMap<>();
        grassMap = new ConcurrentHashMap<>();

        mapVisualizer = new MapVisualizer(this);
        listeners = new ArrayList<>();

        mapId = UUID.randomUUID();
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
        return objectsAt(position).isPresent();
    }

    public Optional<List<Animal>> getAnimalsAt(Vector2d position) {
        if(!animalsMap.containsKey(position))
            return Optional.empty();

        return Optional.of(animalsMap.get(position));
    }

    public Optional<Grass> getGrassAt(Vector2d position) {
        if(!grassMap.containsKey(position))
            return Optional.empty();

        return Optional.of(grassMap.get(position));
    }

    public Optional<List<IWorldElement>> objectsAt(Vector2d position) {
        if(animalsMap.containsKey(position) || grassMap.containsKey(position))
            return Optional.of(
                    Stream.concat(
                            grassMap.containsKey(position) ? Stream.of(grassMap.get(position)) : Stream.empty(),
                            animalsMap.containsKey(position) ? animalsMap.get(position).stream() : Stream.empty()
                    ).toList()
            );
        else
            return Optional.empty();
    }

    public List<IWorldElement> getElements() {
        return new ArrayList<>(
                Streams.concat(
                        grassMap.values().stream().map(grass -> (IWorldElement)grass),
                        animalsMap.values().stream().flatMap(Collection::stream).toList().stream().map(animal -> (IWorldElement)animal)
                ).toList());
    }

    public String toString() {
        Boundary currentBounds = getMapBoundary();
        return mapVisualizer.draw(currentBounds.bottomLeft(), currentBounds.topRight());
    }

    public int getGrassCount() {
        return grassMap.size();
    }

    public int getTakenPositions() {
        return (int)Streams.concat(animalsMap.keySet().stream(), grassMap.keySet().stream()).distinct().count();
    }
}
