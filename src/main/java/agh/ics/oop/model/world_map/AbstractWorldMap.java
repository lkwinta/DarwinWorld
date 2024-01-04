package agh.ics.oop.model.world_map;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.MapVisualizer;
import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.Grass;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;

import java.util.*;
import java.util.stream.Stream;

public abstract class AbstractWorldMap implements IWorldMap, IMoveValidator, IMoveTranslator {
    protected final MapVisualizer mapVisualizer;
    protected final Map<Vector2d, List<Animal>> animalsMap;
    protected final HashMap<Vector2d, Grass> grassMap;
    private final List<IMapChangeListener> listeners;
    private final UUID mapId;
    private final Boundary mapBoundary;
    protected final int width;
    protected final int height;

    protected AbstractWorldMap(int width, int height){
        animalsMap = new HashMap<>();
        grassMap = new HashMap<>();

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

    @Override
    public void place(IWorldElement object) {
        if(object instanceof Animal animal) {
            addToHashMap(animalsMap, animal.getPosition(), animal);
            mapChanged("Animal placed at: " + animal.getPosition());
        } else if (object instanceof Grass grass) {
            grassMap.put(grass.getPosition(), grass);
            mapChanged("Grass placed at: " + grass.getPosition());
        }
    }

    @Override
    public void remove(IWorldElement object) {
        if(object instanceof Animal animal) {
            removeFromHashMap(animalsMap, animal.getPosition(), animal);
            mapChanged("Animal removed from: " + animal.getPosition());
        } else if (object instanceof Grass grass) {
            grassMap.remove(grass.getPosition());
            mapChanged("Grass removed from: " + grass.getPosition());
        }
    }

    @Override
    public void move(IWorldElement object) throws IllegalArgumentException {
        if(!(object instanceof Animal animal))
            throw new IllegalArgumentException("You can't move object that is not an Animal'");

        Vector2d oldPosition = animal.getPosition();

        animal.move(this, this);

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

    @Override
    public boolean isOccupied(Vector2d position) {
        return objectsAt(position).isPresent();
    }

    @Override
    public Optional<List<IWorldElement>> objectsAt(Vector2d position) {
        if (animalsMap.containsKey(position))
            return Optional.of(animalsMap.get(position).stream().map(animal -> (IWorldElement)animal).toList());
        if (grassMap.containsKey(position))
            return Optional.of(List.of(grassMap.get(position)));
        return Optional.empty();
    }

    @Override
    public List<IWorldElement> getElements() {
        return new ArrayList<>(
                Stream.concat(
                        grassMap.values().stream().map(grass -> (IWorldElement)grass),
                        animalsMap.values().stream().flatMap(Collection::stream).toList().stream().map(animal -> (IWorldElement)animal)
                ).toList());
    }

    @Override
    public String toString() {
        Boundary currentBounds = getCurrentBounds();
        return mapVisualizer.draw(currentBounds.bottomLeft(), currentBounds.topRight());
    }

    @Override
    public Boundary getCurrentBounds() {
        return mapBoundary;
    }

    @Override
    public UUID getId() {
        return mapId;
    }
}
