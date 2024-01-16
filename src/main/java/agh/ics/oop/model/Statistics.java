package agh.ics.oop.model;

import agh.ics.oop.model.world_elements.Gene;
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
    private final ObjectProperty<Integer> animalCount = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> grassCount = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> averageEnergy = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> averageLifeSpan = new SimpleObjectProperty<>();
    private final ObjectProperty<Integer> averageChildrenCount = new SimpleObjectProperty<>();
    private final ObjectProperty<List<Gene>> dominateGenome = new SimpleObjectProperty<>();
}
