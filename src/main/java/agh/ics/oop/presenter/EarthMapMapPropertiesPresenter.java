package agh.ics.oop.presenter;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class EarthMapMapPropertiesPresenter {
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;

    public int getWidth(){
        return Integer.parseInt(widthTextField.getText());
    }

    public int getHeight(){
        return Integer.parseInt(heightTextField.getText());
    }
}
