package agh.ics.oop;

import agh.ics.oop.model.Simulation;

@FunctionalInterface
public interface ISimulationTickListener {
    void onSimulationTick();
}
