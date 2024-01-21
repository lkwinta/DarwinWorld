package agh.ics.oop.presenter;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.GenomeView;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.Boundary;
import agh.ics.oop.presenter.util.WorldElementBoxFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static agh.ics.oop.model.util.MathUtil.clamp;

public class SimulationPresenter {
    private static final int MAX_CELL_SIZE = 50;
    private static final int MIN_CELL_SIZE = 3;
    private static final int MAX_AXES_CELL_SIZE = 13;

    private int cellSize = 10;
    private final Vector2d xAxisMask = new Vector2d(1, 0);
    private final Vector2d yAxisMask = new Vector2d(0, 1);
    private AbstractWorldMap worldMap;
    private int width;
    private int height;
    private Simulation simulation;
    private boolean simulationEnded = false;
    private int drawAxes = 0;
    private Node lastSelectedNode = null;
    private boolean animalsClickable = false;

    @FXML
    private GridPane mapGridPane;
    /* Kinda hacky approach to divide view, statisticsViewController name is derived automatically by included fx:id*/
    @FXML
    @SuppressWarnings("unused")
    private StatisticsWindowPresenter statisticsViewController; // It is assigned from simulationWindow.fxml - file included in simulation.fxml
    /* Kinda hacky approach to divide view, statisticsViewController name is derived automatically by included fx:id*/
    @FXML
    @SuppressWarnings("unused")
    private AnimalDetailsPresenter animalDetailsViewController; // It is assigned from simulationWindow.fxml - file included in simulation.fxml
    @FXML
    private ToggleButton pauseToggleButton;
    @FXML
    private ToggleButton resumeToggleButton;
    @FXML
    private Label simulationStatusLabel;
    @FXML
    private ToggleButton showPreferredPositionsToggleButton;
    @FXML
    private ToggleButton showDominantGenotypeAnimalsToggleButton;

    @FXML
    private void onPlayClick() {
        resumeToggleButton.setSelected(true);
        pauseToggleButton.setSelected(false);
        showPreferredPositionsToggleButton.setDisable(true);
        showDominantGenotypeAnimalsToggleButton.setDisable(true);
        showPreferredPositionsToggleButton.setSelected(false);
        showDominantGenotypeAnimalsToggleButton.setSelected(false);

        simulation.resume();
    }

    @FXML
    private void onPauseClick() {
        resumeToggleButton.setSelected(false);
        pauseToggleButton.setSelected(true);
        showPreferredPositionsToggleButton.setDisable(false);
        showDominantGenotypeAnimalsToggleButton.setDisable(false);
        showPreferredPositionsToggleButton.setSelected(false);
        showDominantGenotypeAnimalsToggleButton.setSelected(false);

        simulation.pause();
    }

    private Vector2d getTranslatedNodePosition(Node node){
        Boundary currentBounds = worldMap.getMapBoundary();

        if(GridPane.getColumnIndex(node) == null || GridPane.getRowIndex(node) == null)
            return new Vector2d(currentBounds.bottomLeft().x() - 1, currentBounds.bottomLeft().y() - 1); //new vector out of the bounds

        return new Vector2d(
                GridPane.getColumnIndex(node) + currentBounds.bottomLeft().x() - this.drawAxes,
                this.height - GridPane.getRowIndex(node) - 1 + currentBounds.bottomLeft().y() + this.drawAxes);
    }

    @FXML
    private void onShowDominantGenotypeAnimalsClick() {
        GenomeView dominant = this.simulation.getSimulationStatistics().getDominateGenome().get();
        Set<Vector2d> dominantGenomeAnimalsPositions = worldMap.getElements().stream()
                .filter(Animal.class::isInstance)
                .map(Animal.class::cast)
                .filter(animal -> animal.getGenomeView().equals(dominant))
                .map(Animal::getPosition)
                .collect(Collectors.toSet());

        Stream<Node> nodesToEdit = mapGridPane.getChildren().stream()
                .filter((node) -> dominantGenomeAnimalsPositions.contains(getTranslatedNodePosition(node)));

        if(showDominantGenotypeAnimalsToggleButton.isSelected())
            nodesToEdit.forEach(node -> node.getStyleClass().add("dominant-genotype-highlighted-field"));
        else
            nodesToEdit.forEach(node -> node.getStyleClass().remove("dominant-genotype-highlighted-field"));
    }

    @FXML
    public void onShowPreferredPositionsClick() {
        if(showPreferredPositionsToggleButton.isSelected()){
            Set<Vector2d> preferredPositions = this.simulation.getPreferredPositions();

            for(Vector2d position : preferredPositions){
                Node node = WorldElementBoxFactory.getDummyBox(cellSize);
                node.getStyleClass().add("preferred-position-highlighted-field");
                mapGridPane.add(node,
                        position.x() - worldMap.getMapBoundary().bottomLeft().x() + this.drawAxes,
                        this.height - (position.y() - worldMap.getMapBoundary().bottomLeft().y()) - 1 + this.drawAxes);
            }
        } else {
            List<Node> toRemove = mapGridPane.getChildren().stream()
                    .filter(node -> node.getStyleClass().contains("preferred-position-highlighted-field")).toList();

            mapGridPane.getChildren().removeAll(toRemove);
        }
    }

    private void drawMap() {
        //clear grid
        mapGridPane.getChildren().retainAll(mapGridPane.getChildren().get(0));

        Boundary currentBounds = worldMap.getMapBoundary();

        if(drawAxes > 0)
            createAxes();

        for(IWorldElement element : worldMap.getElements()){
            Node elementImageView;

            if(element instanceof Animal animal) {
                elementImageView = WorldElementBoxFactory.getAnimalBox(
                        animal,
                        cellSize,
                        element == animalDetailsViewController.getTrackedAnimal()
                );
                elementImageView.setOnMouseClicked((event) -> onElementClick(event, new Vector2d(element.getPosition().x(), element.getPosition().y())));
            } else {
                elementImageView = WorldElementBoxFactory.getWorldElementBox(element, cellSize);
            }

            GridPane.setHalignment(elementImageView, HPos.CENTER);

            mapGridPane.add(elementImageView,
                    element.getPosition().x() - currentBounds.bottomLeft().x() + this.drawAxes,
                    this.height - (element.getPosition().y() - currentBounds.bottomLeft().y()) - 1 + this.drawAxes);
        }
    }

    private void onElementClick(MouseEvent event, Vector2d position) {
        if(!animalsClickable)
            return;

        animalDetailsViewController.listAnimals(
                worldMap
                    .objectsAt(position)
                    .filter(Animal.class::isInstance)
                    .map(Animal.class::cast).toList());

        ((Node)event.getSource()).getStyleClass().add("highlighted-field");

        if(lastSelectedNode != null)
            lastSelectedNode.getStyleClass().remove("highlighted-field");

        lastSelectedNode = (Node)event.getSource();
    }

    private void addConstraints() {
        while(mapGridPane.getColumnConstraints().size() < this.width + this.drawAxes){
            mapGridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        while(mapGridPane.getRowConstraints().size() < this.height + this.drawAxes){
            mapGridPane.getRowConstraints().add(new RowConstraints(cellSize));
        }
    }

    private void createAxes() {
        Boundary currentBounds = worldMap.getMapBoundary();

        Label yx = new Label("y/x");
        GridPane.setHalignment(yx, HPos.CENTER);
        mapGridPane.add(yx, 0, 0);

        createAxis(this.width, currentBounds.bottomLeft().x(), 1, xAxisMask);
        createAxis(this.height, currentBounds.topRight().y(), -1, yAxisMask);
    }

    private void createAxis(int size, int start_value, int step, Vector2d axis_mask) {
        int value = start_value;
        for (int i = 1; i <= size; i++){
            Label axis_value = new Label(String.valueOf(value));
            GridPane.setHalignment(axis_value, HPos.CENTER);
            mapGridPane.add(axis_value, i * axis_mask.x(), i * axis_mask.y());
            value += step;
        }
    }

    public void setup(AbstractWorldMap worldMap, int width, int height, Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = worldMap;
        this.width = width;
        this.height = height;


        Rectangle2D screenSize = Screen.getPrimary().getBounds();

        this.cellSize = Math.min(
                (int)Math.round(clamp(screenSize.getHeight()*0.75/height, MIN_CELL_SIZE, MAX_CELL_SIZE)),
                (int)Math.round(clamp(screenSize.getWidth()*0.75/width, MIN_CELL_SIZE, MAX_CELL_SIZE)));

        this.drawAxes = (cellSize >= MAX_AXES_CELL_SIZE ? 1 : 0);

        addConstraints();

        simulation.addListener(Simulation.SimulationEvent.TICK, (sim) -> Platform.runLater(this::drawMap));
        simulation.addListener(Simulation.SimulationEvent.PAUSE, (sim) -> Platform.runLater(this::showSimulationPaused));
        simulation.addListener(Simulation.SimulationEvent.RESUME, (sim) -> Platform.runLater(this::showSimulationResumed));
        simulation.addListener(Simulation.SimulationEvent.END, (sim) -> Platform.runLater(this::showSimulationEnded));

        simulation.addListener(Simulation.SimulationEvent.TICK, (sim) -> Platform.runLater(animalDetailsViewController::updateHandler));
        simulation.addListener(Simulation.SimulationEvent.PAUSE, (sim) -> Platform.runLater(animalDetailsViewController::enableTrackingChange));
        simulation.addListener(Simulation.SimulationEvent.RESUME, (sim) -> Platform.runLater(animalDetailsViewController::disableTrackingChange));

        simulation.addListener(Simulation.SimulationEvent.PAUSE, (sim) -> Platform.runLater(this::enableAnimalsSelection));
        simulation.addListener(Simulation.SimulationEvent.RESUME, (sim) -> Platform.runLater(this::disableAnimalsSelection));
    }

    private void enableAnimalsSelection() {
        this.mapGridPane.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("animal-node"))
                .forEach(node -> node.getStyleClass().add("selectable-animal"));
        this.animalsClickable = true;
    }

    private void disableAnimalsSelection() {
        this.mapGridPane.getChildren().stream()
                .filter(node -> node.getStyleClass().contains("animal-node"))
                .forEach(node -> node.getStyleClass().remove("selectable-animal"));

        this.animalsClickable = false;
    }

    private void showSimulationEnded() {
        simulationStatusLabel.setText("Simulation has ended");
        simulationStatusLabel.setTextFill(Color.RED);
        simulationEnded = true;
    }

    private void showSimulationResumed() {
        if(simulationEnded)
            return;

        simulationStatusLabel.setText("Simulation is running");
        simulationStatusLabel.setTextFill(Color.GREEN);
    }

    private void showSimulationPaused() {
        if(simulationEnded)
            return;

        simulationStatusLabel.setText("Simulation has been paused");
        simulationStatusLabel.setTextFill(Color.ORANGE);
    }

    public void subscribeStatisticsListeners(Statistics statistics) {
        statisticsViewController.subscribeStatisticListeners(statistics);
    }

    public void setupWaterStatistics(boolean waterCountVisible){
        statisticsViewController.setWaterLabelEnabled(waterCountVisible);
    }
}
