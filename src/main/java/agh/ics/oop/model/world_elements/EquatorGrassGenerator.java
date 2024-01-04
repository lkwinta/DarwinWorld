package agh.ics.oop.model.world_elements;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class EquatorGrassGenerator implements Iterator<Grass>, Iterable<Grass> {
    private final int width;
    private final int height;
    private final int equatorYCoordinate;
    private final int equatorHeight;

    public EquatorGrassGenerator(int width, int height, int n){
        //TODO: unique generation?
        this.width = width;
        this.height = height;
        this.equatorYCoordinate = height / 2;
        this.equatorHeight = (int)(0.1*height);
    }

    @Override
    public Iterator<Grass> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Grass next() {
        int x_coordinate = ThreadLocalRandom.current().nextInt(0, width);
        boolean isPreferable = ThreadLocalRandom.current().nextBoolean();

        int y_coordinate;
        if (isPreferable) {
            y_coordinate = ThreadLocalRandom.current().nextInt(equatorYCoordinate - equatorHeight, equatorYCoordinate + equatorHeight);
        } else {
            boolean isUp = ThreadLocalRandom.current().nextBoolean();
            if(isUp)
                y_coordinate = ThreadLocalRandom.current().nextInt(equatorYCoordinate + equatorHeight, height);
            else
                y_coordinate = ThreadLocalRandom.current().nextInt(0, equatorYCoordinate - equatorHeight);
        }

        return new Grass(new Vector2d(x_coordinate, y_coordinate));
    }
}
