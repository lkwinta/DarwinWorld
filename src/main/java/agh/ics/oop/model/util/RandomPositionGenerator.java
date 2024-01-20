package agh.ics.oop.model.util;

import agh.ics.oop.model.world_elements.Vector2d;
import com.google.common.collect.Streams;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class RandomPositionGenerator implements Iterator<Vector2d>, Iterable<Vector2d> {
    private final int min_width;
    private final int min_height;
    private final int max_width;
    private final int max_height;

    public RandomPositionGenerator(Vector2d bottomLeft, Vector2d topRight){
        this.min_width = bottomLeft.x();
        this.min_height = bottomLeft.y();
        this.max_width = topRight.x();
        this.max_height = topRight.y();
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Vector2d next() {
        if(!hasNext())
            throw new NoSuchElementException("Random position generator has no more elements.");

        return new Vector2d(
                ThreadLocalRandom.current().nextInt(min_width, max_width + 1),
                ThreadLocalRandom.current().nextInt(min_height, max_height + 1)
        );
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
