package agh.ics.oop.model.world_elements;

import java.util.Random;

public enum Gene {
    ROTATION_0,
    ROTATION_45,
    ROTATION_90,
    ROTATION_135,
    ROTATION_180,
    ROTATION_225,
    ROTATION_270,
    ROTATION_315;
    private static final Random random = new Random();
    public Gene getNewRandom(){
        int index = random.nextInt(0, values().length);
        while(index == this.ordinal())
            index = random.nextInt(0, values().length);

        return values()[index];
    }

    public static Gene getRandom() {
        return values()[random.nextInt(0, values().length)];
    }

    @Override
    public String toString() {
        return "Gene{" +
                "name=" + this.name() +
                '}';
    }
}
