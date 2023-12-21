package agh.ics.oop.model.world_map;

import agh.ics.oop.model.MoveDirection;
import agh.ics.oop.model.PositionAlreadyOccupiedException;
import agh.ics.oop.model.world_elements.Vector2d;
import agh.ics.oop.model.world_elements.IWorldElement;

import java.util.List;
import java.util.UUID;

/**
 * The interface responsible for interacting with the map of the world.
 * Assumes that MoveDirection classes are defined.
 *
 * @author apohllo, idzik
 */
public interface IWorldMap {

    /**
     * Place a T type object on the map.
     *
     * @param object The animal to place on the map.
     */
    void place(IWorldElement object) throws PositionAlreadyOccupiedException;

    /**
     * Moves an T object (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(IWorldElement object, MoveDirection direction);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an object at a given position.
     *
     * @param position The position of the object.
     * @return animal or null if the position is not occupied.
     */
    IWorldElement objectAt(Vector2d position);

    /**
     * Return all objects on the map
     *
     * @return copy of the list containing all elements on the map
     */
    List<IWorldElement> getElements();

    /**
     * Return current top right and bottom left corner of the map
     *
     * @return container for top right and bottom left vector
     */
    Boundary getCurrentBounds();

    /**
     *
     * @return return ID of the map
     */
    UUID getId();
}