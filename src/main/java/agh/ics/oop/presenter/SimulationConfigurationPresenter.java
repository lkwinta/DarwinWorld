package agh.ics.oop.presenter;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.ModelConfiguration;
import agh.ics.oop.model.Simulation;
import agh.ics.oop.model.Statistics;
import agh.ics.oop.model.util.ConfigurationManager;
import agh.ics.oop.model.world_map.AbstractWorldMap;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SimulationConfigurationPresenter {
    private final SimulationEngine simulationEngine = new SimulationEngine();
    private final List<Stage> stagesList = new ArrayList<>();

    @FXML
    private ListView<String> configurationsListView;

    @FXML
    private TextField mapWidthTextField;

    @FXML
    private TextField mapHeightTextField;

    @FXML
    private TextField startingGrassCountTextField;

    @FXML
    private TextField grassGrowthPerDayTextField;

    @FXML
    private TextField grassEnergyLevelTextField;

    @FXML
    private TextField startingOceanCountTextField;

    @FXML
    private TextField maxOceanSizeTextField;

    @FXML
    private TextField oceanChangeRateTextField;

    @FXML
    private TextField startingAnimalsCount;

    @FXML
    private TextField animalReadyToBreedEnergyLevelTextField;

    @FXML
    private TextField animalStartingEnergy;

    @FXML
    private TextField animalEnergyLossPerMoveTextField;

    @FXML
    private TextField animalEnergyGivenToChildTextField;

    @FXML
    private ComboBox<String> genomeBehaviourSelector;

    @FXML
    private TextField genomeLengthTextField;

    @FXML
    private TextField mutationsCountTextField;

    @FXML
    private TextField millisecondsPerSimulationDayTextField;

    @FXML
    private TextField totalSimulationDaysTextField;

    @FXML
    private TextField configurationNameTextField;

    @FXML
    private ComboBox<String> mapSelector;

    @FXML
    private Pane propertiesPane;

    @FXML
    private Label informationLabel;

    @FXML
    private void onSimulationStartClicked() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getClassLoader().getResource("views/simulation.fxml"));

            BorderPane viewRoot = fxmlLoader.load();
            Stage stage = new Stage();
            configureStage(stage, viewRoot);

            ModelConfiguration configuration = getCurrentConfiguration();
            AbstractWorldMap map = getAbstractWorldMap(fxmlLoader, configuration);

            FXMLLoader fxmlLoaderStatistics = new FXMLLoader();
            fxmlLoaderStatistics.setLocation(getClass().getClassLoader().getResource("views/statistics.fxml"));
            fxmlLoaderStatistics.load();

            StatisticsWindowPresenter statisticsPresenter =  fxmlLoader.getController();
            Statistics statistics = new Statistics();
            statisticsPresenter.subscribeStatisticListeners(statistics);

            Simulation simulation = new Simulation(map, configuration, statistics);

            int id = simulationEngine.runSingleAsync(simulation);

            stage.setOnCloseRequest(event -> simulationEngine.interruptSingleSimulation(id));

            stagesList.add(stage);
            stage.show();
        } catch (NumberFormatException ex) {
            logError("Can't start simulation. Error parsing configuration.");
            System.out.println("Can't start simulation. Error parsing configuration: " + ex.getMessage());
        }
        catch (IOException ex) {
            /* Crash the application, can't continue without necessary view */
            System.out.println("Could not load fxml file: " + ex.getMessage());
            Platform.exit();
        }
    }

    @FXML
    private void onMapSelected() {
        setOceanMapSettings();
    }

    @FXML
    private void onConfigurationLoadClick() {
        if(configurationsListView.getSelectionModel().getSelectedItems().isEmpty()) {
            logError("Please select configuration to load");
            return;
        }

        try {
            String selectedConfiguration = configurationsListView.getSelectionModel().getSelectedItems().get(0);
            ModelConfiguration modelConfiguration = ConfigurationManager.getConfiguration(selectedConfiguration);
            setConfigurationFields(modelConfiguration);
            logInfo("Successfully loaded configuration");

        } catch (Exception ex) {
            logError("Failed to load configuration");
            System.out.printf("Failed to load configuration with error: %s\n", ex.getMessage());
        }
    }

    @FXML
    private void onConfigurationSaveClick() {
        if(configurationNameTextField.getText().isEmpty()) {
            logError("Please enter configuration name");
            return;
        }

        try {
            ModelConfiguration modelConfiguration = getCurrentConfiguration();

            ConfigurationManager.addConfiguration(configurationNameTextField.getText(), modelConfiguration);
            configurationsListView.getItems().add(configurationNameTextField.getText());

            ConfigurationManager.saveConfigurationToFiles();

            logInfo("Successfully saved configuration");
        } catch(NumberFormatException ex) {
            logError("Please enter correct data");
        } catch(IllegalArgumentException ex) {
            logError(ex.getMessage());
        } catch (IOException ex) {
            logError("Failed to save configurations");
            System.out.println("Failed to save configurations with error: " + ex.getMessage());
        }
    }

    @FXML
    private void onConfigurationDeleteClick() {
        if(configurationsListView.getSelectionModel().getSelectedItems().isEmpty()) {
            logError("Please select configuration to delete");
            return;
        }

        String selectedConfiguration = configurationsListView.getSelectionModel().getSelectedItems().get(0);

        try {
            ConfigurationManager.removeConfiguration(selectedConfiguration);
            configurationsListView.getItems().remove(selectedConfiguration);

            ConfigurationManager.saveConfigurationToFiles();

            logInfo("Successfully deleted configuration");
        } catch(IllegalArgumentException ex) {
            logError(ex.getMessage());
        } catch (IOException ex) {
            logError("Failed to save configurations");
            System.out.println("Failed to save configurations with error: " + ex.getMessage());
        }
    }

    private AbstractWorldMap getAbstractWorldMap(FXMLLoader fxmlLoader, ModelConfiguration configuration) {
        SimulationPresenter presenter = fxmlLoader.getController();

        AbstractWorldMap map = configuration.getConstructedMap();
        presenter.setWorldMap(map);

        map.addListener((worldMap, message) -> Platform.runLater(presenter::drawMap));
        map.addListener((worldMap, message) -> Platform.runLater(() -> presenter.setMoveInformationLabel(message)));

        return map;
    }

    public void onConfigurationApplicationClose() throws InterruptedException {
        simulationEngine.interruptAllSimulations();
        simulationEngine.awaitAllSimulationsEnd();

        stagesList.forEach(Stage::close);
    }

    private void setOceanMapSettings(){
        propertiesPane.setDisable(mapSelector.getValue().equals("EarthMap"));
    }

    /*
        Not worth refactoring this duplicated code for the sake of readebility and keeping related things together,
        I don't want to make any connection between app and presenter and i think that creating separate helper class
        for this pice is not worth it
    */
    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public void loadConfigurations() {
        try {
            ConfigurationManager.loadConfigurationsFromFile();
            configurationsListView.getItems().addAll(ConfigurationManager.getConfigurationNames());

        } catch (Exception ex) {
            logError("Failed to load configurations");
            System.out.printf("Failed to load configurations with error: %s\n", ex.getMessage());
        }
    }

    private ModelConfiguration getCurrentConfiguration() throws NumberFormatException {
        ModelConfiguration modelConfiguration = new ModelConfiguration();

        modelConfiguration.setTotalSimulationDays(Integer.parseInt(totalSimulationDaysTextField.getText()));
        modelConfiguration.setMillisecondsPerSimulationDay(Integer.parseInt(millisecondsPerSimulationDayTextField.getText()));

        if(mapSelector.getValue().equals("EarthMap")) {
            modelConfiguration.setMapType(ModelConfiguration.MapType.EARTH_MAP);
        }
        else {
            modelConfiguration.setMapType(ModelConfiguration.MapType.OCEAN_MAP);

            modelConfiguration.setStartingOceanCount(Integer.parseInt(startingOceanCountTextField.getText()));
            modelConfiguration.setMaxOceanSize(Integer.parseInt(maxOceanSizeTextField.getText()));
            modelConfiguration.setOceanChangeRate(Integer.parseInt(oceanChangeRateTextField.getText()));
        }
        modelConfiguration.setMapWidth(Integer.parseInt(mapWidthTextField.getText()));
        modelConfiguration.setMapHeight(Integer.parseInt(mapHeightTextField.getText()));
        modelConfiguration.setStartingGrassCount(Integer.parseInt(startingGrassCountTextField.getText()));
        modelConfiguration.setGrassGrowthPerDay(Integer.parseInt(grassGrowthPerDayTextField.getText()));
        modelConfiguration.setGrassEnergyLevel(Integer.parseInt(grassEnergyLevelTextField.getText()));
        modelConfiguration.setStartingAnimalsCount(Integer.parseInt(startingAnimalsCount.getText()));
        modelConfiguration.setAnimalStartingEnergy(Integer.parseInt(animalStartingEnergy.getText()));
        modelConfiguration.setAnimalEnergyLossPerMove(Integer.parseInt(animalEnergyLossPerMoveTextField.getText()));
        modelConfiguration.setAnimalReadyToBreedEnergyLevel(Integer.parseInt(animalReadyToBreedEnergyLevelTextField.getText()));
        modelConfiguration.setAnimalEnergyGivenToChild(Integer.parseInt(animalEnergyGivenToChildTextField.getText()));

        if(genomeBehaviourSelector.getValue().equals("FullPredestination"))
            modelConfiguration.setGenomeBehaviour(ModelConfiguration.GenomeBehaviour.FULL_PREDESTINATION);
        else
            modelConfiguration.setGenomeBehaviour(ModelConfiguration.GenomeBehaviour.A_BIT_OF_MADNESS);

        modelConfiguration.setGenomeLength(Integer.parseInt(genomeLengthTextField.getText()));
        modelConfiguration.setMutationsCount(Integer.parseInt(mutationsCountTextField.getText()));

        return modelConfiguration;
    }

    private void setConfigurationFields(ModelConfiguration modelConfiguration) {
        totalSimulationDaysTextField.setText(String.valueOf(modelConfiguration.getTotalSimulationDays()));
        millisecondsPerSimulationDayTextField.setText(String.valueOf(modelConfiguration.getMillisecondsPerSimulationDay()));

        if(modelConfiguration.getMapType() == ModelConfiguration.MapType.EARTH_MAP) {
            mapSelector.setValue("EarthMap");
        }
        else {
            mapSelector.setValue("OceanMap");

            startingOceanCountTextField.setText(String.valueOf(modelConfiguration.getStartingOceanCount()));
            maxOceanSizeTextField.setText(String.valueOf(modelConfiguration.getMaxOceanSize()));
            oceanChangeRateTextField.setText(String.valueOf(modelConfiguration.getOceanChangeRate()));
        }
        setOceanMapSettings();

        mapWidthTextField.setText(String.valueOf(modelConfiguration.getMapWidth()));
        mapHeightTextField.setText(String.valueOf(modelConfiguration.getMapHeight()));
        startingGrassCountTextField.setText(String.valueOf(modelConfiguration.getStartingGrassCount()));
        grassGrowthPerDayTextField.setText(String.valueOf(modelConfiguration.getGrassGrowthPerDay()));
        grassEnergyLevelTextField.setText(String.valueOf(modelConfiguration.getGrassEnergyLevel()));
        startingAnimalsCount.setText(String.valueOf(modelConfiguration.getStartingAnimalsCount()));
        animalStartingEnergy.setText(String.valueOf(modelConfiguration.getAnimalStartingEnergy()));
        animalEnergyLossPerMoveTextField.setText(String.valueOf(modelConfiguration.getAnimalEnergyLossPerMove()));
        animalReadyToBreedEnergyLevelTextField.setText(String.valueOf(modelConfiguration.getAnimalReadyToBreedEnergyLevel()));
        animalEnergyGivenToChildTextField.setText(String.valueOf(modelConfiguration.getAnimalEnergyGivenToChild()));

        if(modelConfiguration.getGenomeBehaviour() == ModelConfiguration.GenomeBehaviour.FULL_PREDESTINATION)
            genomeBehaviourSelector.setValue("FullPredestination");
        else
            genomeBehaviourSelector.setValue("ABitOfMadness");

        genomeLengthTextField.setText(String.valueOf(modelConfiguration.getGenomeLength()));
        mutationsCountTextField.setText(String.valueOf(modelConfiguration.getMutationsCount()));
    }

    private void logError(String message) {
        informationLabel.setTextFill(Color.RED);
        informationLabel.setText(message);
    }

    private void logInfo(String message) {
        informationLabel.setTextFill(Color.BLACK);
        informationLabel.setText(message);
    }
}
