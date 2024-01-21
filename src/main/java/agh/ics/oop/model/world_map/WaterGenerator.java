package agh.ics.oop.model.world_map;

import agh.ics.oop.model.util.UniqueRandomPositionGenerator;
import agh.ics.oop.model.world_elements.Vector2d;
import org.javatuples.Pair;

import java.util.*;

public class WaterGenerator {
    private final List<Ocean> oceans;
    private final UniqueRandomPositionGenerator positionGenerator;
    private final int maxSize;
    private final Boundary mapBoundary;

    public WaterGenerator(int maxSize, Boundary mapBoundary){
        this.oceans = new ArrayList<>();
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

    public Pair<Collection<Vector2d>, Collection<Vector2d>> generateSpreadPositions(){
        Pair<Collection<Vector2d>, Collection<Vector2d>> results = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for(Ocean ocean : oceans){
            Pair<Collection<Vector2d>, Collection<Vector2d>> oceanResult = ocean.spreadOcean();
            results.getValue0().addAll(oceanResult.getValue0());
            results.getValue1().addAll(oceanResult.getValue1());
        }

        List<Vector2d> removePositions = results.getValue1()
                .stream()
                .distinct()
                .filter((position) -> oceans.stream().noneMatch(ocean -> ocean.contains(position)))
                .toList();

        return new Pair<>(results.getValue0(), removePositions);
    }

}
