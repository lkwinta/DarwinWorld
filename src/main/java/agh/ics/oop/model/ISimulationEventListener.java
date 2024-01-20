package agh.ics.oop.model;

import agh.ics.oop.model.Simulation;

@FunctionalInterface
public interface ISimulationEventListener {
    void onSimulationEvent(Simulation simulation);
}
