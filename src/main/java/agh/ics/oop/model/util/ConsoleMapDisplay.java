package agh.ics.oop.model.util;

import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.IMapChangeListener;

public class ConsoleMapDisplay implements IMapChangeListener {
    private int updateCounter = 0;
    @Override
    public synchronized void mapChanged(AbstractWorldMap worldMap, String message) {
        System.out.printf("update no: %d map id: %s -> %s \n", ++updateCounter, worldMap.getId().toString(), message);
        System.out.println(worldMap);
    }
}
