package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AnimalPropertyEntryPresenter {
    @FXML
    private TextField yPositionTextField;
    @FXML
    private TextField xPositionTextField;

    public int getXPosition(){
        return Integer.parseInt(xPositionTextField.getText());
    }

    public int getYPosition(){
        return Integer.parseInt(yPositionTextField.getText());
    }
}
