package agh.ics.oop.model;

import agh.ics.oop.ISimulationTickListener;
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
    private final EquatorGrassGenerator grassGenerator;
    private final ModelConfiguration configuration;
    private final Statistics simulationStatistics;
    private int dayNumber = 0;
    private final List<ISimulationTickListener> listeners;

    public Simulation(AbstractWorldMap worldMap, ModelConfiguration configuration, Statistics simulationStatistics) {
        this.worldMap = worldMap;
        this.configuration = configuration;
        this.simulationStatistics = simulationStatistics;
        this.listeners = new ArrayList<>();

        Boundary mapBounds = worldMap.getCurrentBounds();
        RandomPositionGenerator positionGenerator = new RandomPositionGenerator(
                mapBounds.bottomLeft(),
                mapBounds.topRight(),
                this.configuration.getStartingAnimalsCount());

        animalsSet = new HashSet<>(this.configuration.getStartingAnimalsCount());
        positionGenerator.forEach((position) -> {
            Animal animal = new Animal(this.configuration.getAnimalStartingEnergy(), position, this.configuration);
            worldMap.place(animal);
            animalsSet.add(animal);
        });

        grassGenerator = new EquatorGrassGenerator(configuration.getMapWidth(), configuration.getMapHeight());
        grassGenerator.stream().limit(this.configuration.getStartingGrassCount()).forEach(worldMap::place);
    }

    @Override
    public void run() {
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

                updateStatistics();
                notifyListeners();

                Thread.sleep(configuration.getMillisecondsPerSimulationDay());
            }
        } catch(InterruptedException ex){
            System.out.println("Simulation stopped because simulation thread got interrupted!");
        }
    }

    public void addListener(ISimulationTickListener listener){
        listeners.add(listener);
    }

    private void updateStatistics() {
        simulationStatistics.getDayNumber().setValue(++dayNumber);
        simulationStatistics.getAnimalCount().setValue(animalsSet.size());
        simulationStatistics.getGrassCount().setValue(worldMap.getGrassCount());
    }

    private void processRemoveDeadAnimals() {
        List<Animal> deadAnimals = animalsSet.stream()
                .filter(Predicate.not(Animal::isAlive))
                .toList();

        deadAnimals.forEach(worldMap::remove);
        deadAnimals.forEach(animalsSet::remove);
    }

    private void processMoveAnimals() {
        animalsSet.forEach(worldMap::move);
    }

    private void processAnimalsEating() {
        for (Vector2d position : animalsSet.stream().map(Animal::position).collect(Collectors.toSet())) {
            Animal topAnimal = worldMap.getTopAnimalsAt(position)
                    .orElseThrow().stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(1)
                    .toList().get(0);

            Optional<Grass> grass = worldMap.getGrassAt(position);
            if(grass.isEmpty()) continue;

            topAnimal.eat();
            worldMap.remove(grass.get());
            grassGenerator.addFreePosition(position);
        }
    }

    private void processAnimalsReproduction() {
        for (Vector2d position : animalsSet.stream().map(Animal::position).collect(Collectors.toSet())){
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
                .limit(this.configuration.getGrassGrowthPerDay())
                .forEach(worldMap::place);
    }

    private void notifyListeners(){
        listeners.forEach(ISimulationTickListener::onSimulationTick);
    }
}
