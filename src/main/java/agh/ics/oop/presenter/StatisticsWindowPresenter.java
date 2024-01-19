package agh.ics.oop.presenter;

import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.Gene;
import agh.ics.oop.model.world_elements.Genome;
import agh.ics.oop.model.world_elements.GenomeView;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class StatisticsWindowPresenter {
    @FXML
    private Label dayNumberLabel;
    @FXML
    private Label freePositionsLabel;
    @FXML
    private Label animalCountLabel;
    @FXML
    private Label deadAnimalsCount;
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

    private void setFreePositions(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> freePositionsLabel.setText(String.valueOf(newValue)));
    }

    private void setAnimalsCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> animalCountLabel.setText(String.valueOf(newValue)));
    }
    private void setDeadAnimalsCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> deadAnimalsCount.setText(String.valueOf(newValue)));
    }

    private void setGrassCount(Observable observable, Integer oldValue, Integer newValue) {
        Platform.runLater(() -> grassCountLabel.setText(String.valueOf(newValue)));
    }

    private void setAverageEnergy(Observable observable, Double oldValue, Double newValue) {
        Platform.runLater(() -> averageEnergyLabel.setText("%.2f".formatted(newValue)));
    }

    private void setAverageLifeSpan(Observable observable, Double oldValue, Double newValue) {
        Platform.runLater(() -> averageLifeSpanLabel.setText("%.2f".formatted(newValue)));
    }

    private void setAverageChildrenCount(Observable observable, Double oldValue, Double newValue) {
        Platform.runLater(() -> averageChildrenCountLabel.setText("%.2f".formatted(newValue)));
    }

    private void setDominantGenotype(Observable observable, GenomeView oldValue, GenomeView newValue) {
        Platform.runLater(() -> dominantGenotypeLabel.setText(newValue.toString()));
    }

    public void subscribeStatisticListeners(Statistics statistics){
        statistics.getDayNumber().addListener(this::setDayNumber);
        statistics.getFreePositions().addListener(this::setFreePositions);
        statistics.getAnimalsCount().addListener(this::setAnimalsCount);
        statistics.getDeadAnimalsCount().addListener(this::setDeadAnimalsCount);
        statistics.getGrassCount().addListener(this::setGrassCount);
        statistics.getAverageEnergy().addListener(this::setAverageEnergy);
        statistics.getAverageLifeSpan().addListener(this::setAverageLifeSpan);
        statistics.getAverageChildrenCount().addListener(this::setAverageChildrenCount);
        statistics.getDominateGenome().addListener(this::setDominantGenotype);
    }
}
