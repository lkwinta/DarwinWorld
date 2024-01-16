package agh.ics.oop.presenter;

import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.Gene;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class StatisticsWindowPresenter {
    @FXML
    private Label dayNumberLabel;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label grassCountLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label averageLifeSpanLabel;
    @FXML
    private Label averageChildrenCountLabel;
    @FXML
    private Label dominantGenotypeLabel;

    private void setDayNumber(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> dayNumberLabel.setText(String.valueOf(newValue)));
    }

    private void setAnimalCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> animalCountLabel.setText(String.valueOf(newValue)));
    }

    private void setGrassCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> grassCountLabel.setText(String.valueOf(newValue)));
    }

    private void setAverageEnergy(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> averageEnergyLabel.setText(String.valueOf(newValue)));
    }

    private void setAverageLifeSpan(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> averageLifeSpanLabel.setText(String.valueOf(newValue)));
    }

    private void setAverageChildrenCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> averageChildrenCountLabel.setText(String.valueOf(newValue)));
    }

    private void setDominantGenotype(Observable observable, List<Gene> oldValue, List<Gene> newValue) {
        String genomeString = newValue.stream().map(Enum::ordinal).map(String::valueOf)
                .reduce("", (partialString, element) -> partialString + element);
        Platform.runLater(() -> dominantGenotypeLabel.setText(genomeString));
    }

    public void subscribeStatisticListeners(Statistics statistics){
        statistics.getDayNumber().addListener(this::setDayNumber);
        statistics.getAnimalCount().addListener(this::setAnimalCount);
        statistics.getGrassCount().addListener(this::setGrassCount);
        statistics.getAverageEnergy().addListener(this::setAverageEnergy);
        statistics.getAverageLifeSpan().addListener(this::setAverageLifeSpan);
        statistics.getAverageChildrenCount().addListener(this::setAverageChildrenCount);
        statistics.getDominateGenome().addListener(this::setDominantGenotype);
    }
}
