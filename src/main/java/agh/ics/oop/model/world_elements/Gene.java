package agh.ics.oop.model.world_elements;

import lombok.ToString;

import java.util.concurrent.ThreadLocalRandom;

@ToString
public enum Gene {
    ROTATION_0,
    ROTATION_45,
    ROTATION_90,
    ROTATION_135,
    ROTATION_180,
    ROTATION_225,
    ROTATION_270,
    ROTATION_315;

    public Gene getNewRandom(){
        int index = ThreadLocalRandom.current().nextInt(0, values().length);
        while(index == this.ordinal())
            index = ThreadLocalRandom.current().nextInt(0, values().length);

        return values()[index];
    }

    public static Gene getRandom() {
        return values()[ThreadLocalRandom.current().nextInt(0, values().length)];
    }
}
