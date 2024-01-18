package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.Gene;
import agh.ics.oop.model.world_elements.Genome;
import agh.ics.oop.model.world_elements.GenomeView;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class Statistics {
    private final ObjectProperty<Integer> dayNumber = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> freePositions = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> animalsCount = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> deadAnimalsCount = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> grassCount = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> averageEnergy = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> averageLifeSpan = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> averageChildrenCount = new SimpleObjectProperty<>();
    private final ObjectProperty<GenomeView> dominateGenome = new SimpleObjectProperty<>();
}
