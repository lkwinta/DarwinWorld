package agh.ics.oop;

import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.world_elements.EquatorGrassGenerator;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.EarthMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class WorldTest {
    public static void main(String[] args) {
        EarthMap map = new EarthMap(ModelConfiguration.MAP_WIDTH, ModelConfiguration.MAP_HEIGHT);

        System.out.println(map);
        System.out.println(map.toString().chars().filter(c -> c == '*').count());
    }
}
