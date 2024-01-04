package agh.ics.oop.model.world_elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

//TODO: Question: is new Genom random?
public class Genome {
    private final int length;
    private int activeGene;
    private final List<Gene> genes;

    private final IGenomeBehaviour genomeBehaviour;

    private Genome(int length, IGenomeBehaviour genomeBehaviour){
        this.length = length;
        this.activeGene = 0;
        this.genomeBehaviour = genomeBehaviour;

        this.genes = new ArrayList<>(length);
    }

    public static Genome RandomGenome(int length, IGenomeBehaviour genomeBehaviour){
        Genome genome = new Genome(length, genomeBehaviour);

        for(int i = 0; i < length; i++){
            genome.genes.add(Gene.getRandom());
        }

        return genome;
    }

    public Genome combineGenomes(Genome rightGenes, float percentOfLeftGenes) {
        int leftCount = (int)Math.ceil(length * percentOfLeftGenes);

        Genome genome = new Genome(length, this.genomeBehaviour);
        genome.genes.addAll(this.genes.subList(0, leftCount));
        genome.genes.addAll(rightGenes.genes.subList(leftCount, length));

        return genome;
    }

    public void mutate() {
        int mutationsCount = ThreadLocalRandom.current().nextInt(0, length + 1);
        int[] indexes = ThreadLocalRandom.current()
                .ints(0, length)
                .distinct()
                .limit(mutationsCount)
                .toArray();

        for (int index : indexes) {
            genes.set(index, genes.get(index).getNewRandom());
        }
    }

    public Gene getActiveGene(){
        Gene gene = genes.get(activeGene);

        activeGene = genomeBehaviour.shiftGenome(activeGene, length);
        return gene;
    }

    @Override
    public String toString() {
        return "Genome{" +
                "length=" + length +
                ", activeGene=" + activeGene +
                ", genes={" + genes.stream().map(Object::toString).collect(Collectors.joining(", ")) +
                "}}";
    }
}
