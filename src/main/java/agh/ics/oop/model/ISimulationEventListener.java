package agh.ics.oop.model;

@FunctionalInterface
public interface ISimulationEventListener {
    void onSimulationEvent(Simulation simulation);
}
