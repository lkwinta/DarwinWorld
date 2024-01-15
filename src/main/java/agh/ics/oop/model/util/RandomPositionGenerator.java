package agh.ics.oop.model.util;

import agh.ics.oop.model.world_elements.Vector2d;
import com.google.common.collect.Lists;
import com.google.common.collect.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Math.min;

@SuppressWarnings("unused")
public class RandomPositionGenerator implements Iterator<Vector2d>, Iterable<Vector2d> {
    private final int maxCount;
    private int currentIndex = 0;
    private final List<Vector2d> positions = new ArrayList<>();

    public RandomPositionGenerator(Vector2d bottomLeft, Vector2d topRight, int maxCount){
        List<Integer> widthRange = new ArrayList<>(IntStream.range(bottomLeft.x(), topRight.x() + 1).boxed().toList());
        List<Integer> heightRange = new ArrayList<>(IntStream.range(bottomLeft.y(), topRight.x() + 1).boxed().toList());

        Collections.shuffle(widthRange);
        Collections.shuffle(heightRange);

        List<Vector2d> cartesianResult = new ArrayList<>();

        Lists.cartesianProduct(widthRange, heightRange)
                .forEach(position -> cartesianResult.add(new Vector2d(position.get(0), position.get(1))));

        Collections.shuffle(cartesianResult);
        cartesianResult.stream().limit(maxCount).forEach(positions::add);

        this.maxCount = min(maxCount, positions.size());
    }

    @Override
    public boolean hasNext() {
        return currentIndex < maxCount;
    }

    @Override
    public Vector2d next() {
        if(!hasNext())
            throw new NoSuchElementException("Random position generator has no more elements.");

        return positions.get(currentIndex++);
    }

    @Override
    @NotNull
    public Iterator<Vector2d> iterator() {
        return this;
    }

    public Stream<Vector2d> stream(){
        return Streams.stream(iterator());
    }
}
