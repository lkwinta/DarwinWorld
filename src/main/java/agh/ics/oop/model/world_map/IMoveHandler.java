package agh.ics.oop.model.world_map;

import agh.ics.oop.model.world_elements.Vector2d;

public interface IMoveHandler {

    /**
     * Indicate if any object can move to the given position.
     *
     * @param position
     *            The position checked for the movement possibility.
     * @return True if the object can move to that position.
     */
    boolean canMoveTo(Vector2d position);

    Vector2d getTranslatedPosition(Vector2d position);
}