package agh.ics.oop.presenter;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.Animal;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.Boundary;
import agh.ics.oop.util.WorldElementBoxFactory;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

import static agh.ics.oop.model.util.MathUtil.clamp;

public class SimulationPresenter {
    private int cellSize = 10;
    private final Vector2d xAxisMask = new Vector2d(1, 0);
    private final Vector2d yAxisMask = new Vector2d(0, 1);
    private AbstractWorldMap worldMap;
    private int width;
    private int height;
    private Simulation simulation;
    private boolean simulationEnded = false;

    @FXML
    private GridPane mapGridPane;
    /* Kinda hacking approach to divide view, statisticsViewController name is derived automatically by included fx:id*/
    @FXML
    private VBox statisticsView;
    @FXML
    private StatisticsWindowPresenter statisticsViewController; // It is assigned from simulationWindow.fxml - file included in simulation.fxml
    @FXML
    private ToggleButton pauseToggleButton;
    @FXML
    private ToggleButton resumeToggleButton;
    @FXML
    private Label simulationStatusLabel;
    @FXML
    private BorderPane rootBorderPane;

    private void drawMap() {
        //clear grid
        mapGridPane.getChildren().retainAll(mapGridPane.getChildren().get(0));

        Boundary currentBounds = worldMap.getMapBoundary();

        createAxes();

        for(IWorldElement element : worldMap.getElements()){
            Node elementImageView = element instanceof Animal animal
                    ? WorldElementBoxFactory.getAnimalBox(animal, cellSize) :
                        WorldElementBoxFactory.getWorldElementBox(element, cellSize);
            GridPane.setHalignment(elementImageView, HPos.CENTER);

            mapGridPane.add(elementImageView,
                    element.position().x() + 1 - currentBounds.bottomLeft().x(),
                    this.height - (element.position().y() - currentBounds.bottomLeft().y()));
        }
    }

    private void addConstraints() {
        while(mapGridPane.getColumnConstraints().size() <= this.width){
            mapGridPane.getColumnConstraints().add(new ColumnConstraints(cellSize));
        }

        while(mapGridPane.getRowConstraints().size() <= this.height){
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
            //axis_value.setMaxSize(cellSize, cellSize);
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
                (int)Math.round(clamp(screenSize.getHeight()*0.75/height, 5, 50)),
                (int)Math.round(clamp(screenSize.getWidth()*0.75/width, 5, 50)));

        addConstraints();
        createAxes();

        simulation.addListener(Simulation.SimulationEvent.TICK, () -> Platform.runLater(this::drawMap));
        simulation.addListener(Simulation.SimulationEvent.PAUSE, () -> Platform.runLater(this::showSimulationPaused));
        simulation.addListener(Simulation.SimulationEvent.RESUME, () -> Platform.runLater(this::showSimulationResumed));
        simulation.addListener(Simulation.SimulationEvent.END, () -> Platform.runLater(this::showSimulationEnded));
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

    @FXML
    private void onPlayClick() {
        resumeToggleButton.setSelected(true);
        pauseToggleButton.setSelected(false);

        simulation.resume();
    }

    @FXML
    private void onPauseClick() {
        resumeToggleButton.setSelected(false);
        pauseToggleButton.setSelected(true);

        simulation.pause();
    }
}
