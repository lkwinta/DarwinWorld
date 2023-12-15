package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

import java.util.*;
import java.util.stream.IntStream;

public class RandomPositionGenerator implements Iterator<Vector2d>, Iterable<Vector2d> {
    private final int maxCount;
    private final List<Integer> widthRange;
    private final List<Integer> heightRange;
    private int currentIndex = 0;

    public RandomPositionGenerator(Vector2d bottomLeft, Vector2d topRight, int maxCount){
        this.maxCount = maxCount;

        widthRange = new ArrayList<>(IntStream.range(bottomLeft.getX(), topRight.getX() + 1).boxed().toList());
        heightRange = new ArrayList<>(IntStream.range(bottomLeft.getY(), topRight.getX() + 1).boxed().toList());

        Collections.shuffle(widthRange);
        Collections.shuffle(heightRange);
    }

    @Override
    public boolean hasNext() {
        return currentIndex < maxCount;
    }

    @Override
    public Vector2d next() {
        if(!hasNext())
            throw new NoSuchElementException();

        return new Vector2d(widthRange.get(currentIndex), heightRange.get(currentIndex++));
    }

    @Override
    public Iterator<Vector2d> iterator() {
        return this;
    }
}
