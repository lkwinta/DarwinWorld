package agh.ics.oop.model.world_map;

import agh.ics.oop.model.util.UniqueRandomPositionGenerator;
import agh.ics.oop.model.world_elements.Vector2d;

import java.util.*;

public class WaterGenerator {
    private final Set<Ocean> oceans;
    private final UniqueRandomPositionGenerator positionGenerator;
    private final int maxSize;
    private final Boundary mapBoundary;

    public WaterGenerator(int maxSize, Boundary mapBoundary){
        this.oceans = new HashSet<>();
        this.positionGenerator = new UniqueRandomPositionGenerator(
                mapBoundary.bottomLeft(), mapBoundary.topRight(), mapBoundary.getArea());
        this.maxSize = maxSize;
        this.mapBoundary = mapBoundary;
    }

    public List<Vector2d> generateStartingWaterPositions(int count){
        List<Vector2d> positions = positionGenerator.stream()
                .limit(count)
                .toList();

        oceans.addAll(positions
                .stream()
                .map(position -> new Ocean(position, maxSize, mapBoundary))
                .toList());
        return positions;
    }

    public List<Collection<Vector2d>> generateSpreadPositions(){
        List<Collection<Vector2d>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        result.add(new ArrayList<>());

        for(Ocean ocean : oceans){
            List<Collection<Vector2d>> oceanResult = ocean.spreadOcean();

            result.get(0).addAll(oceanResult.get(0));
            result.get(1).addAll(oceanResult.get(1));
        }

        return result;
    }

}
