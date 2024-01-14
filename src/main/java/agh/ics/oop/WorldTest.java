package agh.ics.oop;

import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.util.ConsoleMapDisplay;
import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.world_elements.ABitOfMadnessBehaviour;
import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.FullPredestinationBehaviour;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.EarthMap;

import java.awt.image.AffineTransformOp;
import java.util.stream.StreamSupport;

public class WorldTest {
    public static void main(String[] args) {
        AbstractWorldMap map = new EarthMap(ModelConfiguration.MAP_WIDTH, ModelConfiguration.MAP_HEIGHT);


        //ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();

        //map.addListener(mapDisplay);

        //Simulation simulation = new Simulation(map, new FullPredestinationBehaviour(), 1000);

        //simulation.run();
    }
}
