package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_map.IWorldMap;

public interface IMapChangeListener {
    void mapChanged(IWorldMap worldMap, String message);
}
