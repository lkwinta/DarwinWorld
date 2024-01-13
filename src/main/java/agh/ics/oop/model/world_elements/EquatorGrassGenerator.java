package agh.ics.oop.model.world_elements;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EquatorGrassGenerator implements Iterator<Grass>, Iterable<Grass> {
    private final int equatorYCoordinate;
    private final int equatorHeight;
    private final Set<Vector2d> freeEquatorPositions;
    private final Set<Vector2d> freeOtherPositions;

    public EquatorGrassGenerator(int width, int height){
        //TODO: unique generation?
        this.equatorYCoordinate = height / 2;
        this.equatorHeight = (int)Math.ceil(0.1*height);

        this.freeOtherPositions = new HashSet<>(width*height - 2*equatorHeight*width);
        this.freeEquatorPositions = new HashSet<>(2*equatorHeight*width);

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++){
                 addFreePosition(new Vector2d(x, y));
            }
        }

        System.out.println(freeOtherPositions.size());
        System.out.println(freeEquatorPositions.size());
    }

    public void addFreePosition(Vector2d position) {
        if(isPositionInEquator(position))
            freeEquatorPositions.add(position);
        else
            freeOtherPositions.add(position);
    }

    private boolean isPositionInEquator(Vector2d position){
        return position.getY() > equatorYCoordinate - equatorHeight && position.getY() < equatorYCoordinate + equatorHeight;
    }

    @Override
    public Iterator<Grass> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return !freeEquatorPositions.isEmpty() || !freeOtherPositions.isEmpty();
    }

    private Vector2d popElementFromSet(Set<Vector2d> set, int index){
        if(index >= set.size())
            throw new IndexOutOfBoundsException("Index was outside of given set");

        int current_index = 0;
        Vector2d position_to_return = null;

        for(Vector2d position : set){
            if(current_index == index){
                position_to_return = position;
                break;
            }
            current_index++;
        }

        set.remove(position_to_return);
        return position_to_return;
    }

    @Override
    public Grass next() {
        boolean isPreferable = ThreadLocalRandom.current().nextFloat() < 0.8;

        if((isPreferable || freeOtherPositions.isEmpty()) && !freeEquatorPositions.isEmpty()){
            return new Grass(popElementFromSet(freeEquatorPositions, ThreadLocalRandom.current().nextInt(0, freeEquatorPositions.size())));
        } else {
            return new Grass(popElementFromSet(freeOtherPositions, ThreadLocalRandom.current().nextInt(0, freeOtherPositions.size())));
        }
    }

    public Stream<Grass> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
