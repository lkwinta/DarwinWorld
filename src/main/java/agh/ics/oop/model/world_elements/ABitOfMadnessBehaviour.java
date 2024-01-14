package agh.ics.oop.model.world_elements;

import java.util.concurrent.ThreadLocalRandom;

public class ABitOfMadnessBehaviour implements IGenomeBehaviour {
    @Override
    public int shiftGenome(int old_gene_index, int genome_length) {
        float chance = ThreadLocalRandom.current().nextFloat();

        if (chance <= 0.8 ) {
            return (old_gene_index + 1) % genome_length;
        } else {
            boolean isLeft = ThreadLocalRandom.current().nextBoolean();
            if (isLeft) {
                return ThreadLocalRandom.current().nextInt(0, old_gene_index);
            } else {
                return ThreadLocalRandom.current().nextInt((old_gene_index + 1) % genome_length, genome_length);
            }
        }
    }
}
