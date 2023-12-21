package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

public interface IMoveTranslator {
    Vector2d getTranslatedPosition(Vector2d position);
}
