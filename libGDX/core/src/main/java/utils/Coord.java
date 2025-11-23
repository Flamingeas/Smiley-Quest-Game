package utils;

/**
 * Represents a 2D coordinate with floating-point precision.
 * Provides basic getters, setters, and string representation.
 */
public class Coord {
    private float x;
    private float y;

    /**
     * Constructs a coordinate with specified x and y values.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Coord(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /** @return the x-coordinate */
    public float getX() {
        return x;
    }

    /** @return the y-coordinate */
    public float getY() {
        return y;
    }

    /** Sets the x-coordinate */
    public void setX(float x) {
        this.x = x;
    }

    /** Sets the y-coordinate */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns a string representation of the coordinate in the form (x, y)
     * @return string representing the coordinate
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
