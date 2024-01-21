package agh.ics.oop.model.world_elements;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Class representing 2D point/Vector in integer cooridnate system
 *
 * @param x -- GETTER --
 *          Getter for X coordinate
 * @param y -- GETTER --
 *          Getter for X coordinate
 */
@SuppressWarnings("unused")
public record Vector2d(int x, int y) {
    /**
     * Constructor taking pair of integers and storing them as final
     * vector/point coordinates
     *
     * @param x X coordinate of vector
     * @param y Y coordinate of vector
     */
    public Vector2d {
    }

    /**
     * Checks if vector is precessing other vector,
     *
     * @param other Vector2d for comparison
     * @return true if both coordinates are less or equal other's coordinates, otherwise false
     */
    public boolean precedes(Vector2d other) {
        return this.x <= other.x && this.y <= other.y;
    }

    /**
     * Checks if vector is following other vector,
     *
     * @param other Vector2d for comparison
     * @return true if both coordinates are greater or equal other's coordinates, otherwise false
     */
    public boolean follows(Vector2d other) {
        return this.x >= other.x && this.y >= other.y;
    }

    /**
     * Adds coordinates of other vector to the vector
     *
     * @param other Vector whose coordinates will be added
     * @return new Vector2d with added coordinates
     */
    public Vector2d add(Vector2d other) {
        return new Vector2d(this.x + other.x, this.y + other.y);
    }

    /**
     * Subtracts coordinates of other vector to the vector
     *
     * @param other Vector whose coordinates will be added
     * @return new Vector2d with added coordinates
     */
    public Vector2d subtract(Vector2d other) {
        return new Vector2d(this.x - other.x, this.y - other.y);
    }

    /**
     * Negates vector
     *
     * @return Vector2d with coordinates*(-1)
     */
    public Vector2d opposite() {
        return new Vector2d(-this.x, -this.y);
    }

    public Vector2d upperRight(Vector2d other) {
        return new Vector2d(max(other.x, this.x), max(other.y, this.y));
    }

    public Vector2d lowerLeft(Vector2d other) {
        return new Vector2d(min(other.x, this.x), min(other.y, this.y));
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(this.x, this.y);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Vector2d vec)
            return vec.x == this.x && vec.y == this.y;
        return false;
    }

    public List<Vector2d> getNeighbours() {
        return List.of(
                new Vector2d(this.x, this.y - 1),
                new Vector2d(this.x - 1, this.y),
                new Vector2d(this.x + 1, this.y),
                new Vector2d(this.x, this.y + 1)
        );
    }
}
