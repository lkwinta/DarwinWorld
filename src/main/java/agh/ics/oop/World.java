package agh.ics.oop;

import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.world_elements.*;
import agh.ics.oop.model.world_map.EarthMap;

import java.util.*;
import java.util.stream.StreamSupport;

public class World {
    public static void main(String[] args) {
        EarthMap map = new EarthMap(ModelConfiguration.MAP_WIDTH, ModelConfiguration.MAP_HEIGHT);
        List<Vector2d> positions = new ArrayList<>();

        EquatorGrassGenerator grassGenerator = new EquatorGrassGenerator(ModelConfiguration.MAP_WIDTH, ModelConfiguration.MAP_HEIGHT, 100);
        StreamSupport.stream(grassGenerator.spliterator(), false).limit(50).forEach(map::place);

        System.out.println(map);

        ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();
        map.addListener(mapDisplay);

        Simulation simulation = new Simulation(positions, map, 1000);
        simulation.run();
    }
}