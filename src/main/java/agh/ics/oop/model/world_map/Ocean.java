package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

public class Ocean {
    private final Vector2d startingPoint;
    private final int maxRadius;
    private int currentRadius;
    private final Set<Vector2d> waterPositions;
    private final Boundary mapBoundary;

    public Ocean(Vector2d startingPoint, int maxRadius, Boundary mapBoundary) {
        this.startingPoint = startingPoint;
        this.maxRadius = maxRadius;
        this.waterPositions = new HashSet<>();
        this.mapBoundary = mapBoundary;

        this.waterPositions.add(startingPoint);
        this.currentRadius = 0;
    }

    private List<Vector2d> getBorderPositions(){
        List<Vector2d> borderPositions = new ArrayList<>();

        for(Vector2d position : waterPositions){
            List<Vector2d> possiblePositions = position.getNeighbours().stream()
                    .filter(mapBoundary::inBounds)
                    .filter(Predicate.not(waterPositions::contains))
                    .toList();

            if(possiblePositions.isEmpty())
                continue;

            borderPositions.add(position);
        }

        return borderPositions;
    }

    public List<Collection<Vector2d>> spreadOcean() {
        HashSet<Vector2d> newWaterPositions = new HashSet<>();
        List<Vector2d> positionsToRemove = new ArrayList<>();

        boolean shouldSpread = ThreadLocalRandom.current().nextFloat() >= 2.0/5.0;
        List<Vector2d> borderPositions = getBorderPositions();

        if(shouldSpread){
            if(currentRadius + 1 >= maxRadius)
                return new ArrayList<>(List.of(newWaterPositions, positionsToRemove));

            borderPositions.stream()
                    .map(Vector2d::getNeighbours)
                    .flatMap(Collection::stream)
                    .distinct()
                    .filter(mapBoundary::inBounds)
                    .filter(Predicate.not(waterPositions::contains))
                    .forEach(newWaterPositions::add);

            waterPositions.addAll(newWaterPositions);
            this.currentRadius += 1;
        } else {
            positionsToRemove.addAll(
                    borderPositions.stream()
                            .filter(Predicate.not(startingPoint::equals))
                            .toList());
            positionsToRemove.forEach(waterPositions::remove);
            this.currentRadius -= 1;
        }

        return new ArrayList<>(List.of(newWaterPositions, positionsToRemove));
    }
}
