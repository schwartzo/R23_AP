package Team4450.Robot23.pathfinder.math;

/**
 * A vertex in a two-dimensional graph.
 */
public class Vertex2d extends Vertex<Vertex2d>
{

    private double x, y;

    /**
     * Instantiates a vertex using X and Y coordinates.
     * @param x The x coordinate of the vertex.
     * @param y The y coordinate of the vertex.
     */
    public Vertex2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the vertex.
     * @return The x coordinate of the vertex.
     */
    public double getX()
    {
        return x;
    }

    /**
     * Returns the y coordinate of the vertex.
     * @return The y coordinate of the vertex.
     */
    public double getY()
    {
        return y;
    }

    /**
     * Returns a vertex which is offset by polar coordinates.
     * @param distance The distance to offset by.
     * @param rotation The angle in radians to offset at.
     * @return A new vertex which is offset by the specified amount.
     */
    public Vertex2d polarOffset(double distance, double rotation)
    {
        return new Vertex2d(x + distance * Math.cos(rotation), y + distance * Math.sin(rotation));
    }

    @Override
    public Vertex2d plus(Vertex2d other)
    {
        return new Vertex2d(x + other.getX(), y + other.getY());
    }

    @Override
    public Vertex2d minus(Vertex2d other)
    {
        return new Vertex2d(x - other.getX(), y - other.getY());
    }

    @Override
    public double dot(Vertex2d other)
    {
        return x * other.getX() + y * other.getY();
    }

    @Override
    public double distance(Vertex2d other)
    {
        return Math.hypot(x - other.getX(), y - other.getY());
    }

    @Override
    public String toString()
    {
        return Double.toString(x) + " " + Double.toString(y);
    }
}
