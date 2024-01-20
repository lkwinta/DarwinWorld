package agh.ics.oop.model.world_elements;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class GenomeView {
    private final Genome genome;

    public GenomeView(Genome genome) {
        this.genome = genome;
    }

    @Override
    public String toString() {
        return genome.toString();
    }

    public int getActiveGeneIndex() {
        return genome.getActiveGeneIndex();
    }
}
