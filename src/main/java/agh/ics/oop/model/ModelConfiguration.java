package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.ABitOfMadnessBehaviour;
import agh.ics.oop.model.world_elements.FullPredestinationBehaviour;
import agh.ics.oop.model.world_elements.IGenomeBehaviour;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.EarthMap;
import agh.ics.oop.model.world_map.OceanMap;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModelConfiguration {
    public enum MapType {
        EARTH_MAP ,
        OCEAN_MAP
    }

    public enum GenomeBehaviour {
        FULL_PREDESTINATION,
        A_BIT_OF_MADNESS
    }

    private MapType mapType = MapType.EARTH_MAP;
    private int mapWidth = 10;
    private int mapHeight = 10;
    private int startingGrassCount = 10;
    private int grassGrowthPerDay = 1;
    private int grassEnergyLevel = 5;
    private int startingAnimalsCount = 5;
    private int animalStartingEnergy = 1;
    private int animalEnergyLossPerMove = 1;
    private int animalReadyToBreedEnergyLevel = 50;
    private int animalEnergyGivenToChild = 20;
    private GenomeBehaviour genomeBehaviour = GenomeBehaviour.FULL_PREDESTINATION;
    private int genomeLength = 8;
    private int minimalMutationsCount = 1;
    private int maximalMutationsCount = 3;
    private int startingOceanCount = 3;
    private int maxOceanSize = 5;
    private int oceanChangeRate = 10;
    private int millisecondsPerSimulationDay = 500;
    private int totalSimulationDays = Integer.MAX_VALUE;

    public AbstractWorldMap getConstructedMap(){
        return switch(mapType){
            case EARTH_MAP -> new EarthMap(mapWidth, mapHeight);
            case OCEAN_MAP -> new OceanMap(mapWidth, mapHeight);
        };
    }

    public IGenomeBehaviour getConstructedBehaviour(){
        return switch(genomeBehaviour){
            case A_BIT_OF_MADNESS -> new ABitOfMadnessBehaviour();
            case FULL_PREDESTINATION -> new FullPredestinationBehaviour();
        };
    }

    public void validate() throws IllegalArgumentException {
        if(mapWidth <= 0 || mapHeight <= 0)
            throw new IllegalArgumentException("Map width and height must be positive");
        if(mapWidth > 200 || mapHeight > 200)
            throw new IllegalArgumentException("Map width and height must be less or equal to 200");

        if(startingGrassCount < 0)
            throw new IllegalArgumentException("Starting grass count must be non-negative");
        if(grassGrowthPerDay < 0)
            throw new IllegalArgumentException("Grass growth per day must be non-negative");
        if(grassEnergyLevel <= 0)
            throw new IllegalArgumentException("Grass energy level must be positive");

        if(startingAnimalsCount < 0)
            throw new IllegalArgumentException("Starting animals count must be non-negative");
        if(animalStartingEnergy <= 0)
            throw new IllegalArgumentException("Animal starting energy must be positive");
        if(animalEnergyLossPerMove <= 0)
            throw new IllegalArgumentException("Animal energy loss per move must be positive");
        if(animalEnergyLossPerMove >= animalStartingEnergy)
            throw new IllegalArgumentException("Animal energy loss per move must less then starting energy");
        if(animalReadyToBreedEnergyLevel <= 0)
            throw new IllegalArgumentException("Animal ready to breed energy level must be positive");
        if(animalEnergyGivenToChild <= 0)
            throw new IllegalArgumentException("Animal energy given to child must be positive");
        if(animalEnergyGivenToChild > animalReadyToBreedEnergyLevel)
            throw new IllegalArgumentException("Animal energy given to child less or equal to animal ready to breed energy level");

        if(genomeLength <= 0)
            throw new IllegalArgumentException("Genome length must be positive");
        if(minimalMutationsCount < 0)
            throw new IllegalArgumentException("Minimal mutations count must be non-negative");
        if(maximalMutationsCount < 0)
            throw new IllegalArgumentException("Maximal mutations count must be non-negative");
        if(minimalMutationsCount > maximalMutationsCount)
            throw new IllegalArgumentException("Minimal mutations count must be less or equal to maximal mutations count");
        if(maximalMutationsCount > genomeLength)
            throw new IllegalArgumentException("Maximal mutations count must be less or equal to genome length");

        if(startingOceanCount < 0)
            throw new IllegalArgumentException("Starting ocean count must be non-negative");
        if(maxOceanSize <= 0)
            throw new IllegalArgumentException("Maximal ocean size must be positive");
        if(oceanChangeRate <= 0)
            throw new IllegalArgumentException("Ocean change rate must be positive");

        if(millisecondsPerSimulationDay <= 0)
            throw new IllegalArgumentException("Milliseconds per simulation day must be positive");
    }
}
