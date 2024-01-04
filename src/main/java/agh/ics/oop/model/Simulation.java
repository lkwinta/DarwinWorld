package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.IGenomeBehaviour;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.IWorldMap;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.interrupted;

public class Simulation implements Runnable {
    /** using array list because we iterate over the animals list
     * so continuous memory gives us extra performance compared to
     * LinkedList, also we are not adding more elements at runtime,
     * so we don't benefit from LinkedList fast append
     * */
    private final List<Animal> animalsList;
    private final IWorldMap worldMap;
    private final int timeout;

    public Simulation(List<Vector2d> startingPositions, IWorldMap worldMap, IGenomeBehaviour genomeBehaviour, int timeout){
        this.worldMap = worldMap;
        this.timeout = timeout;

        animalsList = new ArrayList<>(startingPositions.size());
        for(Vector2d position : startingPositions) {
            Animal animal = new Animal(ModelConfiguration.START_ENERGY, position, genomeBehaviour);

            try {
                worldMap.place(animal);
                animalsList.add(animal);
            } catch (PositionAlreadyOccupiedException ex){
                System.out.println("Tried to place animal on occupied position");
            }

        }
    }

    @Override
    public void run() {
        int animalIndex = 0;
        /*
        * Handling exception thrown by Thread.sleep(), can't throw exception further because run is overridden function
        * */
        try {
            while(!interrupted()) {
                worldMap.move(animalsList.get(animalIndex));
                animalIndex = ++animalIndex % animalsList.size();
                Thread.sleep(timeout);
            }
        } catch(InterruptedException ex){
            System.out.println("Simulation stopped because simulation thread got interrupted!");
        }
    }

    private void moveAnimals() {
        for(Animal animal : animalsList){
            worldMap.move(animal);
        }
    }

    private void removeDeadAnimals() {
        for(int i = 0; i < animalsList.size(); i++){
            Animal animal = animalsList.get(i);
            if(!animal.isAlive()){
                worldMap.remove(animalsList.get(i));
                animalsList.remove(i);
                i--;
            }
        }
    }

    public Animal getAnimal(int index){
        return animalsList.get(index);
    }
}
