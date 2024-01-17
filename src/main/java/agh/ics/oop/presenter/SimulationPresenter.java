package agh.ics.oop.presenter;

import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.world_elements.IWorldElement;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import agh.ics.oop.model.world_map.Boundary;
import agh.ics.oop.util.WorldElementBoxFactory;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import lombok.Setter;

public class SimulationPresenter {

    private final static int CELL_WIDTH = 40;
    private final static int CELL_HEIGHT = 40;

    private final Vector2d xAxisMask = new Vector2d(1, 0);
    private final Vector2d yAxisMask = new Vector2d(0, 1);
    private AbstractWorldMap worldMap;
    private int width;
    private int height;

    @FXML
    private GridPane mapGridPane;
    @FXML
    private Label moveInformation;
    @FXML
    private StatisticsWindowPresenter statisticsViewController;

    public void drawMap() {
        clearGrid();

        Boundary currentBounds = worldMap.getCurrentBounds();

        createAxes();

        for(IWorldElement element : worldMap.getElements()){
            VBox elementBox = WorldElementBoxFactory.getWorldElementBox(element);
            GridPane.setHalignment(elementBox, HPos.CENTER);
            mapGridPane.add(elementBox,
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
        Boundary currentBounds = worldMap.getCurrentBounds();

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

    private void clearGrid() {
        mapGridPane.getChildren().retainAll(mapGridPane.getChildren().get(0)); // hack to retain visible grid lines
    }

    public void setup(AbstractWorldMap worldMap, int width, int height) {
        this.worldMap = worldMap;
        this.width = width;
        this.height = height;

        addConstraints();
        createAxes();
    }

    public void setMoveInformationLabel(String text) {
        moveInformation.setText(text);
    }

    public void subscribeStatisticsListeners(Statistics statistics) {
        statisticsViewController.subscribeStatisticListeners(statistics);
    }
}
