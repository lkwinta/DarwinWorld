package agh.ics.oop.model.world_map;

@FunctionalInterface
public interface IMapChangeListener {
    void mapChanged(AbstractWorldMap worldMap, String message);
}
