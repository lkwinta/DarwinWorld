package agh.ics.oop;

import com.google.common.collect.Streams;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import java.util.List;

public class WorldTest { // czy to jest do czegoś potrzebne?
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

//        Statistics statistics = new Statistics();
//        statistics.getDayNumber().addListener((observable, oldValue, newValue) -> {
//            System.out.println("Old value: " + oldValue);
//            System.out.println("New value: " + newValue);
//        });
//
//        statistics.getDayNumber().setValue(5);

        //simulation.run();

//        Genome genome = new Genome(List.of(
//                Gene.ROTATION_0,
//                Gene.ROTATION_90,
//                Gene.ROTATION_180
//        ), new FullPredestinationBehaviour());
//
//        Genome genome1 = new Genome(List.of(
//                Gene.ROTATION_0,
//                Gene.ROTATION_90,
//                Gene.ROTATION_180
//        ), new FullPredestinationBehaviour());
//
//        System.out.println(genome);
//        System.out.println(genome1);
//        System.out.println(genome.equals(genome1));
//
//        GenomeView genomeView = new GenomeView(genome);
//        GenomeView genomeView1 = new GenomeView(genome);
//
//        System.out.println(genomeView);
//        System.out.println(genomeView1);
//        System.out.println(genomeView.equals(genomeView1));

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS");
//        System.out.println(LocalDateTime.now().format(formatter));
//        System.out.println(LocalDateTime.now().format(formatter));
//        System.out.println(LocalDateTime.now().format(formatter));
//        System.out.println(LocalDateTime.now().format(formatter));
//        try { Thread.sleep(10); } catch(Exception ex) {}
//        System.out.println(LocalDateTime.now().format(formatter));

        List<String> s1 = List.of("a", "b", "c");
        List<String> s2 = List.of("1", "2", "3");

        Streams.concat(s1.stream(), s2.stream()).forEach(System.out::println);
    }
}
