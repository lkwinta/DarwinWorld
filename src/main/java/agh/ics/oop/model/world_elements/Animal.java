package agh.ics.oop.model.world_elements;

import agh.ics.oop.model.world_map.IMoveTranslator;
import agh.ics.oop.model.world_map.IMoveValidator;
import agh.ics.oop.model.world_map.MapDirection;

//TODO: Question: are new animals spawned at the beginign?

public class Animal implements IWorldElement, Comparable<Animal> {
    private Vector2d position;
    private MapDirection orientation;
    private int energyLevel;
    private final Genome genome;

    public Animal(int initialEnergyLevel){
        this.energyLevel = initialEnergyLevel;
        this.genome = Genome.RandomGenome();
    }

    private Animal(int initialEnergyLevel, Genome initialGenome) {
        this.energyLevel = initialEnergyLevel;
        this.genome = initialGenome;
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return false;
    }

    public void move(IMoveTranslator moveTranslator, IMoveValidator moveValidator){
        orientation = orientation.shift(genome.getActiveGene());
        Vector2d newPosition = moveTranslator.getTranslatedPosition(position.add(orientation.toUnitVector()));

        if(moveValidator.canMoveTo(newPosition)){
            position = newPosition;
        }
    }

    public void changeEnergy(int energyDelta){
        energyLevel += energyDelta;
    }

    public boolean isAlive() {
        return energyLevel > 0;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    @Override
    public int compareTo(Animal o) {
        return Integer.compare(energyLevel, o.energyLevel);
    }

    public Animal bread(Animal other){
        //TODO : implement breading mechanism
        return new Animal(0);
    }
}
