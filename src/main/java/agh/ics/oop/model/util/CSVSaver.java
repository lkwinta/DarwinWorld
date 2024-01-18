package agh.ics.oop.model.util;

import agh.ics.oop.ISimulationEventListener;
import agh.ics.oop.model.Simulation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVSaver implements ISimulationEventListener {
    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public synchronized void onSimulationEvent(Simulation simulation) {
        File logFile = new File("simulation_logs/%s.csv".formatted(simulation.getSimulationId()));

        boolean firstLine = false;

        try {
            logFile.getParentFile().mkdirs();
            if(logFile.createNewFile()) {
                firstLine = true;
            }
        } catch (IOException ex){
            System.out.printf("Failed to open a csv file: %s, with error: %s\n", logFile.getPath(), ex.getMessage());
        }

        try (FileWriter writer = new FileWriter(logFile, true)) {
            if(firstLine)
                writer.append(simulation.getSimulationStatistics().getCsvNames()).append(System.lineSeparator());

            writer.append(simulation.getSimulationStatistics().toString()).append(System.lineSeparator());
        } catch (IOException ex) {
            System.out.printf("Failed to write to a csv file: %s, with error: %s\n", logFile.getPath(), ex.getMessage());
        }
    }
}
