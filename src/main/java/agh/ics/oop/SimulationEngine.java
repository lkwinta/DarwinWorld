package agh.ics.oop;

import agh.ics.oop.model.Simulation;

import java.util.ArrayList;
import java.util.List;

public class SimulationEngine {
    private final List<Simulation> simulationList;
    private final List<Thread> simulationThreadsList;

    public SimulationEngine(){
        simulationList = new ArrayList<>();
        simulationThreadsList = new ArrayList<>();
    }

    public SimulationEngine(List<Simulation> simulations){
        simulationList = new ArrayList<>(simulations);
        simulationThreadsList = new ArrayList<>(simulationList.size());
    }

    public void runAllSync(){
        simulationList.forEach(Simulation::run);
    }

    public void runAllAsync()  {
        simulationList.forEach((simulation) -> {
            Thread thread = new Thread(simulation);
            thread.start();
            simulationThreadsList.add(thread);
        });
    }

    public void awaitAllSimulationsEnd() throws InterruptedException {
        for(Thread thread : simulationThreadsList){
            if(thread.isAlive())
                thread.join();
        }
    }


    public int runSingleAsync(Simulation simulation){
        Thread thread = new Thread(simulation);
        thread.start();
        simulationThreadsList.add(thread);

        return simulationThreadsList.size() - 1;
    }

    public void interruptAllSimulations() {
        simulationThreadsList.forEach(Thread::interrupt);
    }

    public void interruptSingleSimulation(int index){
        simulationThreadsList.get(index).interrupt();
    }
}
