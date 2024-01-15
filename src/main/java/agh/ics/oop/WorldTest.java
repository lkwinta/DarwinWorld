package agh.ics.oop;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.util.ConfigurationManager;
import agh.ics.oop.model.world_elements.FullPredestinationBehaviour;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.EarthMap;

public class WorldTest {
    public static void main(String[] args) {
        //AbstractWorldMap map = new EarthMap(ModelConfigurationTest.MAP_WIDTH, ModelConfigurationTest.MAP_HEIGHT);


        //ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();

        //map.addListener(mapDisplay);

        //Simulation simulation = new Simulation(map, new FullPredestinationBehaviour(), 1000);

        try {
            ConfigurationManager.loadConfigurationsFromFile();
        } catch (Exception ex) {
            System.out.printf("Failed to load configurations with error: %s\n", ex.getMessage());
        }

        System.out.println(ConfigurationManager.getConfigurationNames());
        //simulation.run();
    }
}
