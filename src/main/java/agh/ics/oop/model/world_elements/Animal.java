package agh.ics.oop.model.world_elements;

import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.world_map.IMoveHandler;
import agh.ics.oop.model.world_map.MapDirection;

import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.max;

//TODO: Question: are new animals spawned with random genome and random orientation ?

public class Animal implements IWorldElement, Comparable<Animal> {
    private Vector2d position;
    private MapDirection orientation;
    private int energyLevel;
    private final Genome genome;

    public Animal(int initialEnergyLevel, Vector2d initialPosition, IGenomeBehaviour genomeBehaviour){
        this.genome = Genome.RandomGenome(ModelConfiguration.ANIMAL_GENES_LENGTH, genomeBehaviour);
        System.out.println(genome);
        initializeAnimal(initialEnergyLevel, initialPosition);
    }

    private Animal(int initialEnergyLevel, Genome initialGenome, Vector2d initialPosition) {
        this.genome = initialGenome;
        initializeAnimal(initialEnergyLevel, initialPosition);
        System.out.println(genome);
    }

    private void initializeAnimal(int initialEnergyLevel, Vector2d initialPosition){
        this.energyLevel = initialEnergyLevel;
        this.position = initialPosition;
        this.orientation = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, MapDirection.values().length)];
    }

    @Override
    public Vector2d getPosition() {
        return position;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return position.equals(this.position);
    }

    public void move(IMoveHandler moveHandler){
        orientation = orientation.shift(genome.getActiveGene().ordinal());
        Vector2d newPosition = moveHandler.getTranslatedPosition(position.add(orientation.toUnitVector()));

        if(moveHandler.canMoveTo(newPosition)){
            position = newPosition;
        } else {
            orientation = orientation.shift(Gene.ROTATION_180.ordinal());
        }

        changeEnergy(-ModelConfiguration.ANIMAL_ENERGY_LOSS_PER_MOVE);
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

    public Animal breed(Animal other){
        int side = ThreadLocalRandom.current().nextInt(0, 2);

        float genesRatio = max(this.energyLevel, other.energyLevel)/(float)(this.energyLevel + other.energyLevel);

        Genome newGenome = getChildGenome(other, side, genesRatio);

        this.changeEnergy(-ModelConfiguration.ANIMAL_ENERGY_LOSS_PER_CHILD);
        other.changeEnergy(-ModelConfiguration.ANIMAL_ENERGY_LOSS_PER_CHILD);

        return new Animal(2*ModelConfiguration.ANIMAL_ENERGY_LOSS_PER_CHILD, newGenome, other.getPosition());
    }

    private Genome getChildGenome(Animal other, int side, float genesRatio) {
        Genome strongerGenome = this.energyLevel > other.energyLevel ? this.genome : other.genome;
        Genome weakerGenome = this.energyLevel > other.energyLevel ? other.genome : this.genome;

        Genome newGenome = switch (side) {
            case 0 -> strongerGenome.combineGenomes(weakerGenome, genesRatio);
            case 1 -> weakerGenome.combineGenomes(strongerGenome, 1 - genesRatio);
            default -> throw new IllegalStateException("Unexpected value: " + side);
        };

        newGenome.mutate();
        return newGenome;
    }

    public boolean canBreed() {
        return energyLevel > ModelConfiguration.ANIMAL_READY_TO_BREED_ENERGY;
    }

    @Override
    public String toString(){
        return switch (orientation) {
            case NORTH -> "^";
            case SOUTH -> "v";
            case WEST -> "<";
            case EAST -> ">";
            case NORTH_WEST -> "7";
            case NORTH_EAST -> "F";
            case SOUTH_WEST -> "J";
            case SOUTH_EAST -> "L";
        };
    }

    @Override
    public String getResourceName() {
        return switch (orientation) {
            case NORTH -> "north.png";
            case SOUTH -> "south.png";
            case WEST -> "west.png";
            case NORTH_WEST -> "north_west.png";
            case NORTH_EAST -> "north_east.png";
            case EAST -> "east.png";
            case SOUTH_WEST -> "south_west.png";
            case SOUTH_EAST -> "south_east.png";
        };
    }
}
