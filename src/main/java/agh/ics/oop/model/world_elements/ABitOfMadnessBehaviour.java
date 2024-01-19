package agh.ics.oop.model.world_elements;

import java.util.concurrent.ThreadLocalRandom;

public class ABitOfMadnessBehaviour implements IGenomeBehaviour {
    @Override
    public int shiftGenome(int old_gene_index, int genome_length) {
        float chance = ThreadLocalRandom.current().nextFloat();

        if (chance <= 0.8 ) {
            return (old_gene_index + 1) % genome_length;
        } else {
            int index = ThreadLocalRandom.current().nextInt(0, genome_length);
            while (index == old_gene_index)
                index = ThreadLocalRandom.current().nextInt(0, genome_length);

            return index;
        }
    }
}
