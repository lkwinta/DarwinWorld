package agh.ics.oop.model.world_elements;

import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.world_map.IMoveHandler;
import agh.ics.oop.model.world_map.MapDirection;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static agh.ics.oop.model.util.MathUtil.clamp;
import static agh.ics.oop.model.util.MathUtil.getColorGradient;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class Animal implements IWorldElement, Comparable<Animal> {
    @Getter
    private Vector2d position;
    @Getter
    private MapDirection orientation;
    @Getter
    private int energyLevel;
    private final Genome genome;
    private final ModelConfiguration configuration;
    @Getter
    private int age;
    private final List<Animal> children;
    @Getter
    private int grassEaten = 0;
    @Getter
    @Setter
    private int diedAt = -1;

    public Animal(int initialEnergyLevel, Vector2d initialPosition, ModelConfiguration configuration){
        this.children = new ArrayList<>();
        this.configuration = configuration;

        this.genome = Genome.RandomGenome(this.configuration.getGenomeLength(), this.configuration.getConstructedBehaviour());
        initializeAnimal(initialEnergyLevel, initialPosition);
    }

    private Animal(int initialEnergyLevel, Genome initialGenome, Vector2d initialPosition, ModelConfiguration configuration) {
        this.children = new ArrayList<>();
        this.configuration = configuration;

        this.genome = initialGenome;
        initializeAnimal(initialEnergyLevel, initialPosition);
    }

    private void initializeAnimal(int initialEnergyLevel, Vector2d initialPosition){
        this.energyLevel = initialEnergyLevel;
        this.position = initialPosition;
        this.orientation = MapDirection.values()[ThreadLocalRandom.current().nextInt(0, MapDirection.values().length)];
        this.age = 0;
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

        this.genome.nextGene();
        this.energyLevel -= this.configuration.getAnimalEnergyLossPerMove();
    }

    public void age(){
        this.age++;
    }

    public void eat(){
        energyLevel += this.configuration.getGrassEnergyLevel();
        grassEaten++;
    }

    public boolean isAlive() {
        return energyLevel > 0;
    }

    @Override
    public int compareTo(Animal o) {
        return Integer.compare(energyLevel, o.energyLevel);
    }

    public double getHealth() {
        double percent = (double)energyLevel/configuration.getAnimalStartingEnergy();
        return clamp(percent, 0.0, 1.0);
    }

    public Animal breed(Animal other){
        int side = ThreadLocalRandom.current().nextInt(0, 2);

        float genesRatio = max(this.energyLevel, other.energyLevel)/(float)(this.energyLevel + other.energyLevel);

        Genome newGenome = getChildGenome(other, side, genesRatio);

        this.energyLevel -= this.configuration.getAnimalEnergyGivenToChild();
        other.energyLevel -= this.configuration.getAnimalEnergyGivenToChild();

        Animal child = new Animal(2*this.configuration.getAnimalEnergyGivenToChild(), newGenome, other.getPosition(), this.configuration);
        this.children.add(child);

        return child;
    }

    private Genome getChildGenome(Animal other, int side, float genesRatio) {
        Genome strongerGenome = this.energyLevel > other.energyLevel ? this.genome : other.genome;
        Genome weakerGenome = this.energyLevel > other.energyLevel ? other.genome : this.genome;

        Genome newGenome = switch (side) {
            case 0 -> strongerGenome.combineGenomes(weakerGenome, genesRatio);
            case 1 -> weakerGenome.combineGenomes(strongerGenome, 1 - genesRatio);
            default -> throw new IllegalStateException("Unexpected value: " + side);
        };

        newGenome.mutate(this.configuration.getMinimalMutationsCount(), this.configuration.getMaximalMutationsCount());
        return newGenome;
    }

    public boolean canBreed() {
        return energyLevel > this.configuration.getAnimalReadyToBreedEnergyLevel();
    }

    public GenomeView getGenomeView() {
        return new GenomeView(genome);
    }

    public int getChildrenCount() {
        return children.size();
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
//        return switch (orientation) {
//            case NORTH -> "north.png";
//            case SOUTH -> "south.png";
//            case WEST -> "west.png";
//            case NORTH_WEST -> "north_west.png";
//            case NORTH_EAST -> "north_east.png";
//            case EAST -> "east.png";
//            case SOUTH_WEST -> "south_west.png";
//            case SOUTH_EAST -> "south_east.png";
//        };
        return "owlbear.png";
    }

    @Override
    public Color getColor() {
        return getColorGradient(getHealth(), Color.RED, Color.LIME);
    }

    public int getDescendantsCount() {
        Animal current = this;
        int descendantsCount = 0;
        Stack<Animal> animals = new Stack<>();
        Set<Animal> counted = new HashSet<>();

        animals.push(current);

        while(!animals.isEmpty()){
            current = animals.pop();
            counted.add(current);

            for(Animal child : current.children){
                if(!counted.contains(child)){
                    animals.push(child);
                    descendantsCount++;
                }
            }
        }

        return descendantsCount;
    }
}
