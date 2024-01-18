package agh.ics.oop.presenter;

import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.Boundary;
import agh.ics.oop.util.WorldElementBoxFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class SimulationPresenter {
    private final static int CELL_WIDTH = 20;
    private final static int CELL_HEIGHT = 20;
    private final Vector2d xAxisMask = new Vector2d(1, 0);
    private final Vector2d yAxisMask = new Vector2d(0, 1);
    private AbstractWorldMap worldMap;
    private int width;
    private int height;
    private Simulation simulation;

    @FXML
    private GridPane mapGridPane;
    /* Kinda hacking approach to divide view, statisticsViewController name is derived automatically by included fx:id*/
    @FXML
    private Parent statisticsView;
    @FXML
    private StatisticsWindowPresenter statisticsViewController; // It is assigned from simulationWindow.fxml - file included in simulation.fxml
    @FXML
    private ToggleButton pauseToggleButton;
    @FXML
    private ToggleButton resumeToggleButton;

    public void drawMap() {
        //clear grid
        mapGridPane.getChildren().retainAll(mapGridPane.getChildren().get(0));

        Boundary currentBounds = worldMap.getMapBoundary();

        createAxes();

        for(IWorldElement element : worldMap.getElements()){
            ImageView elementImageView = WorldElementBoxFactory.getWorldElementBox(element);
            GridPane.setHalignment(elementImageView, HPos.CENTER);
            mapGridPane.add(elementImageView,
                    element.position().x() + 1 - currentBounds.bottomLeft().x(),
                    this.height - (element.position().y() - currentBounds.bottomLeft().y()));
        }
    }

    private void addConstraints() {
        while(mapGridPane.getColumnConstraints().size() <= this.width){
            mapGridPane.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        }

        while(mapGridPane.getRowConstraints().size() <= this.height){
            mapGridPane.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
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

        addConstraints();
        createAxes();
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
