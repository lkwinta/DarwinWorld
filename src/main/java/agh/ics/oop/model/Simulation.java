package agh.ics.oop.model;

import agh.ics.oop.model.util.RandomPositionGenerator;
import agh.ics.oop.model.world_elements.*;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.Boundary;
import agh.ics.oop.model.world_map.EquatorGrassGenerator;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.Thread.interrupted;

public class Simulation implements Runnable {
    private final Set<Animal> animalsSet;
    private final AbstractWorldMap worldMap;
    private final int timeout;
    private final EquatorGrassGenerator grassGenerator;

    public Simulation(AbstractWorldMap worldMap, IGenomeBehaviour genomeBehaviour, int timeout){
        this.worldMap = worldMap;
        this.timeout = timeout;


        Boundary mapBounds = worldMap.getCurrentBounds();
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(
                mapBounds.bottomLeft(),
                mapBounds.topRight(),
                ModelConfiguration.ANIMALS_STARTING_COUNT);

        animalsSet = new HashSet<>(ModelConfiguration.ANIMALS_STARTING_COUNT);
        for(Vector2d position : positionGenerator) {
            Animal animal = new Animal(ModelConfiguration.ANIMAL_STARTING_ENERGY, position, genomeBehaviour);
            worldMap.place(animal);
            animalsSet.add(animal);
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
                processRemoveDeadAnimals();
                processMoveAnimals();
                processAnimalsEating();
                processAnimalsReproduction();
                processGrowNewGrass();

                Thread.sleep(timeout);
            }
        } catch(InterruptedException ex){
            System.out.println("Simulation stopped because simulation thread got interrupted!");
        }
    }

    private void processRemoveDeadAnimals() {
        List<Animal> deadAnimals = animalsSet.stream()
                .filter(Predicate.not(Animal::isAlive))
                .toList();

        deadAnimals.forEach(worldMap::remove);
        deadAnimals.forEach(animalsSet::remove);
    }

    private void processMoveAnimals() {
        for(Animal animal : animalsSet){
            worldMap.move(animal);
        }
    }

    private void processAnimalsEating() {
        for (Vector2d position : animalsSet.stream().map(Animal::getPosition).collect(Collectors.toSet())) {
            Animal topAnimal = worldMap.getTopAnimalsAt(position)
                    .orElseThrow().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(1)
                    .toList().get(0);

            Optional<Grass> grass = worldMap.getGrassAt(position);
            if(grass.isEmpty()) continue;

            topAnimal.changeEnergy(ModelConfiguration.ANIMAL_ENERGY_GAIN_PER_PLANT);
            worldMap.remove(grass.get());
            grassGenerator.addFreePosition(position);
        }
    }

    private void processAnimalsReproduction() {
        for (Vector2d position : animalsSet.stream().map(Animal::getPosition).collect(Collectors.toSet())){
            List<Animal> canBreed = worldMap
                    .getTopAnimalsAt(position)
                    .orElseThrow().stream()
                    .filter(Animal::canBreed)
                    .sorted(Comparator.reverseOrder())
                    .toList();

            for(int i = 1; i < canBreed.size(); i+= 2){
                Animal firstAnimal = canBreed.get(i - 1);
                Animal secondAnimal = canBreed.get(i);

                Animal child = firstAnimal.breed(secondAnimal);
                worldMap.place(child);
                animalsSet.add(child);
            }
        }
    }

    private void processGrowNewGrass() {
        grassGenerator.stream()
                .limit(ModelConfiguration.GRASS_GROWTH_PER_EVOLUTION)
                .forEach(worldMap::place);
    }
}
