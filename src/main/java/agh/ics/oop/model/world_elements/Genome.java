package agh.ics.oop.model.world_elements;

import java.util.List;

//TODO: Question: is new Genom random?
public class Genome {
    private final int length;
    private int activeGene;
    private List<Integer> genes;

    public Genome(int length){
        this.length = length;
        activeGene = 0;
    }

    public Genome combineGenomes(Genome rightGenes, float percentOfLeftGenes) {
        int leftCount = (int)Math.ceil(length * percentOfLeftGenes);
        int rightCount = (int)Math.floor(length * (1 - percentOfLeftGenes));

        
    }

    public int getActiveGene(){
        int gene = genes.get(activeGene);

        activeGene = (activeGene + 1)%length;
        return gene;
    }
}
