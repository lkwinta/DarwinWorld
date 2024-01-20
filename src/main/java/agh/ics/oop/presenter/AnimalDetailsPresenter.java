package agh.ics.oop.presenter;

import agh.ics.oop.model.world_elements.Animal;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.List;

public class AnimalDetailsPresenter {
    @FXML
    private Label infoLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private HBox diedHBox;
    @FXML
    private Label currentEnergy;
    @FXML
    private Label grassEatenLabel;
    @FXML
    private Label childCountLabel;
    @FXML
    private Label descendantsCountLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label diedAtLabel;
    @FXML
    private ListView<Animal> animalDetailsListView;
    @FXML
    private ListView<Text> genotypeListView;

    @Getter
    private Animal trackedAnimal = null;
    private int previousIndex;

    @FXML
    private void initialize() {
        animalDetailsListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, animal, t1) -> {
            if(t1 != null)
                setTrackedAnimal(t1);
        });
    }

    public void listAnimals(List<Animal> animals){
        animalDetailsListView.getItems().clear();
        animals.forEach(animal -> animalDetailsListView.getItems().add(animal));
    }

    public void setTrackedAnimal(Animal animal) {
        this.trackedAnimal = animal;

        this.genotypeListView.getItems().clear();
        this.genotypeListView.getItems()
                .addAll(animal.getGenomeView().toString().chars()
                        .mapToObj(c -> (char) c)
                        .map(String::valueOf)
                        .map(Text::new)
                        .toList());

        this.previousIndex = 0;
        this.infoLabel.setVisible(false);
        this.infoLabel.setManaged(false);

        updateHandler();
    }

    public void updateHandler() {
        if(this.trackedAnimal == null)
            return;

        this.positionLabel.setText(this.trackedAnimal.getPosition().toString());
        this.genotypeListView.getItems().get(this.previousIndex).getStyleClass().clear();
        this.genotypeListView.getItems()
                .get(this.trackedAnimal
                    .getGenomeView()
                    .getActiveGeneIndex())
                .getStyleClass().add("highlighted_gene");
        this.previousIndex = this.trackedAnimal.getGenomeView().getActiveGeneIndex();
        this.genotypeListView.scrollTo(Math.max(0, this.previousIndex - 1));

        this.currentEnergy.setText(String.valueOf(this.trackedAnimal.getEnergyLevel()));
        this.grassEatenLabel.setText(String.valueOf(this.trackedAnimal.getGrassEaten()));
        this.childCountLabel.setText(String.valueOf(this.trackedAnimal.getChildrenCount()));
        this.descendantsCountLabel.setText(String.valueOf(this.trackedAnimal.getDescendantsCount()));

        this.ageLabel.setText(String.valueOf(this.trackedAnimal.getAge()));

        if(this.trackedAnimal.isAlive()){
            this.diedHBox.setManaged(false);
            this.diedHBox.setVisible(false);
        } else {
            this.diedHBox.setManaged(true);
            this.diedHBox.setVisible(true);

            this.diedAtLabel.setText(String.valueOf(this.trackedAnimal.getDiedAt()));
        }
    }

    public void enableTrackingChange() {
        this.animalDetailsListView.setDisable(false);
        if(trackedAnimal == null){
            this.infoLabel.setVisible(true);
            this.infoLabel.setManaged(true);
            this.infoLabel.setText("No animal selected");
        }
    }

    public void disableTrackingChange() {
        this.animalDetailsListView.setDisable(true);
        if(trackedAnimal == null){
            this.infoLabel.setVisible(true);
            this.infoLabel.setManaged(true);
            this.infoLabel.setText("To select animal for tracking please pause the simulation!");
        }
    }

    @FXML
    private void onClearTrackingClick() {
        this.trackedAnimal = null;

        this.positionLabel.setText("");
        this.genotypeListView.getItems().clear();
        this.currentEnergy.setText("");
        this.grassEatenLabel.setText("");
        this.childCountLabel.setText("");
        this.descendantsCountLabel.setText("");
        this.ageLabel.setText("");
        this.diedAtLabel.setText("");

        this.infoLabel.setVisible(true);
        this.infoLabel.setManaged(true);
        this.infoLabel.setText("No animal selected");
    }
}
