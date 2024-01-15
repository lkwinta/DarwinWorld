package agh.ics.oop.model.world_map;

public interface IMapChangeListener {
    void mapChanged(AbstractWorldMap worldMap, String message);
}
