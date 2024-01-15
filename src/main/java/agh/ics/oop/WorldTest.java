package agh.ics.oop;

import agh.ics.oop.model.util.ConfigurationManager;
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
