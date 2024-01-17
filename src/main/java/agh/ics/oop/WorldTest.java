package agh.ics.oop;

import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.util.ConfigurationManager;
import agh.ics.oop.model.world_elements.Gene;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WorldTest {
    @Getter
    static ObjectProperty<Integer> objectProperty = new SimpleObjectProperty<>();

    public static void main(String[] args) {
        //AbstractWorldMap map = new EarthMap(ModelConfigurationTest.MAP_WIDTH, ModelConfigurationTest.MAP_HEIGHT);


        //ConsoleMapDisplay mapDisplay = new ConsoleMapDisplay();

        //map.addListener(mapDisplay);

        //Simulation simulation = new Simulation(map, new FullPredestinationBehaviour(), 1000);

//        try {
//            ConfigurationManager.loadConfigurationsFromFile();
//        } catch (Exception ex) {
//            System.out.printf("Failed to load configurations with error: %s\n", ex.getMessage());
//        }
//
//        System.out.println(ConfigurationManager.getConfigurationNames());


//    getObjectProperty().addListener((observable, oldValue, newValue) -> {
//        System.out.println("Old value: " + oldValue);
//        System.out.println("New value: " + newValue);
//    });
//
//    objectProperty.setValue(1);

        Statistics statistics = new Statistics();
        statistics.getDayNumber().addListener((observable, oldValue, newValue) -> {
            System.out.println("Old value: " + oldValue);
            System.out.println("New value: " + newValue);
        });

        statistics.getDayNumber().setValue(5);

        //simulation.run();
    }
}
