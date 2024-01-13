package agh.ics.oop;

import agh.ics.oop.presenter.SimulationConfigurationPresenter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader configurationLoader = new FXMLLoader();
            configurationLoader.setLocation(getClass().getClassLoader().getResource("views/simulationConfiguration.fxml"));

            BorderPane viewRoot = configurationLoader.load();

            configureStage(primaryStage, viewRoot);
            SimulationConfigurationPresenter presenter = configurationLoader.getController();

            presenter.loadPropertiesPane();

            primaryStage.setOnCloseRequest(event -> {
                /*
                we can't throw InterruptedException any further because of lambda, we have to handle it
                 */
                try {
                    presenter.onApplicationClose();
                }
                catch(InterruptedException ex) {
                    System.out.println("Interrupted exception while closing application");
                }
                Platform.exit();
            });

            primaryStage.show();

        } catch (IOException ex) {
            /* Can't continue without necessary view, exiting */
            System.out.println("Could not load fxml file: " + ex.getMessage());
        }
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation Configuration");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
