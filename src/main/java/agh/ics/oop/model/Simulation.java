package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.*;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.IWorldMap;
import com.sun.webkit.Timer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.lang.Thread.interrupted;

public class Simulation implements Runnable {
    private final Set<Animal> animalsList;
    private final IWorldMap worldMap;
    private final int timeout;
    private final EquatorGrassGenerator grassGenerator;

    public Simulation(List<Vector2d> startingPositions, IWorldMap worldMap, IGenomeBehaviour genomeBehaviour, int timeout){
        this.worldMap = worldMap;
        this.timeout = timeout;

        animalsList = new HashSet<>(startingPositions.size());
        for(Vector2d position : startingPositions) {
            Animal animal = new Animal(ModelConfiguration.START_ENERGY, position, genomeBehaviour);
            worldMap.place(animal);
            animalsList.add(animal);
        }

        grassGenerator = new EquatorGrassGenerator(worldMap.getWidth(), worldMap.getHeight());
        grassGenerator.stream().limit(ModelConfiguration.GRASS_STARTING_COUNT).forEach(worldMap::place);
    }

    @Override
    public void run() {
        int animalIndex = 0;
        /*
        * Handling exception thrown by Thread.sleep(), can't throw exception further because run is overridden function
        * */
        try {
            while(!interrupted()) {
                removeDeadAnimals();
                moveAnimals();
                Thread.sleep(timeout);
            }
        } catch(InterruptedException ex){
            System.out.println("Simulation stopped because simulation thread got interrupted!");
        }
    }

    private void removeDeadAnimals() {
        animalsList.removeIf(Predicate.not(Animal::isAlive));
    }

    private void moveAnimals() {
        for(Animal animal : animalsList){
            worldMap.move(animal);
        }
    }

    private void processAnimalsEating() {
        for (Vector2d position : animalsList.stream().map(Animal::getPosition).collect(Collectors.toSet())) {
            //worldMap.objectsAt(position).ifPresent();
        }
    }

    private void growNewGrass() {
        grassGenerator.stream()
                .limit(ModelConfiguration.GRASS_GROWTH_PER_EVOLUTION)
                .forEach(worldMap::place);
    }
}
