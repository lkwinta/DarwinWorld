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
    private int mutationsCount = 1;
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
}
