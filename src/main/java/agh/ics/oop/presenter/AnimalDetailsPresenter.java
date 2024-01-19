package agh.ics.oop.presenter;

import agh.ics.oop.model.world_elements.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AnimalDetailsPresenter {
    @FXML
    private HBox aliveHBox;
    @FXML
    private HBox diedHBox;
    @FXML
    private TextFlow genomeTextFlow;
    @FXML
    private Label currentEnergy;
    @FXML
    private Label grassEatenLabel;
    @FXML
    private Label childCountLabel;
    @FXML
    private Label descendantsCountLabel;
    @FXML
    private Label isAliveLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label diedAtLabel;
    @FXML
    private ListView<String> animalDetailsListView;

    private Animal trackedAnimal = null;
    private int previousIndex;

    public void listAnimals(List<Animal> animals){
        animalDetailsListView.getItems().clear();
        animals.forEach(animal -> animalDetailsListView.getItems().add("Animal at: " + animal.getPosition() + " has " + animal.getEnergyLevel() + " energy"));
    }

    public void setTrackedAnimal(Animal animal) {
        this.trackedAnimal = animal;

        this.genomeTextFlow.getChildren().clear();
        this.genomeTextFlow.getChildren()
                .addAll(animal.getGenomeView().toString().chars()
                        .mapToObj(c -> (char) c)
                        .map(String::valueOf)
                        .map(Text::new)
                        .toList());

        previousIndex = 0;
        updateHandler();

//
//        this.grassEatenLabel.setText(String.valueOf(animal.getGrassEaten()));
//        this.childCountLabel.setText(String.valueOf(animal.getChildren().size()));
//        this.descendantsCountLabel.setText(String.valueOf(animal.getDescendantsCount()));
//        this.isAliveLabel.setText(String.valueOf(animal.isAlive()));
//        this.ageLabel.setText(String.valueOf(animal.getAge()));
//        this.diedAtLabel.setText(String.valueOf(animal.getDiedAt()));
    }

    public void updateHandler() {
        if(this.trackedAnimal == null)
            return;

        this.genomeTextFlow.getChildren().get(this.previousIndex).setStyle("-fx-fill: black");
        this.genomeTextFlow.getChildren()
                .get(this.trackedAnimal
                    .getGenomeView()
                    .getActiveGeneIndex())
                .setStyle("-fx-fill: magenta; -fx-effect: dropshadow( gaussian, rgba(83, 34, 117, 0.8) , 7, 0.5 , 0 , 0);");
        this.previousIndex = this.trackedAnimal.getGenomeView().getActiveGeneIndex();

        this.currentEnergy.setText(String.valueOf(this.trackedAnimal.getEnergyLevel()));
        this.grassEatenLabel.setText(String.valueOf(this.trackedAnimal.getGrassEaten()));
        this.childCountLabel.setText(String.valueOf(this.trackedAnimal.getChildrenCount()));
        this.descendantsCountLabel.setText(String.valueOf(this.trackedAnimal.getDescendantsCount()));

        this.isAliveLabel.setText(String.valueOf(this.trackedAnimal.isAlive()));
        if(this.trackedAnimal.isAlive()){
            this.diedHBox.setManaged(false);
            this.diedHBox.setVisible(false);
            this.aliveHBox.setManaged(true);
            this.aliveHBox.setVisible(true);

            this.ageLabel.setText(String.valueOf(this.trackedAnimal.getAge()));
        } else {
            this.diedHBox.setManaged(true);
            this.diedHBox.setVisible(true);
            this.aliveHBox.setManaged(false);
            this.aliveHBox.setVisible(false);

            this.diedAtLabel.setText(String.valueOf(this.trackedAnimal.getDiedAt()));
        }
    }

    public void removeTrackedAnimal() {
        this.trackedAnimal = null;
    }
}
