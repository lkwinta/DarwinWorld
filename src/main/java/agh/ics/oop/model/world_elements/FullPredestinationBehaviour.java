package agh.ics.oop.model.world_elements;

public class FullPredestinationBehaviour implements IGenomeBehaviour {
    @Override
    public int shiftGenome(int old_gene_index, int genome_length) {
        return (old_gene_index + 1) % genome_length;
    }
}
