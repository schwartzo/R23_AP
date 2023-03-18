package Team4450.Robot23.pathfinder;

import Team4450.Robot23.pathfinder.math.Vertex2d;
import edu.wpi.first.math.geometry.Translation2d;

public interface State2d<T> extends State<T>
{
    
    /**
     * Gets the X value used by the state.
     * @return The X value used by the state.
     */
    public double getX();

    /**
     * Gets the Y value used by the state.
     * @return The Y value used by the state.
     */
    public double getY();

    /**
     * Gets the state as a translation.
     * @return A new translation with identical X and Y values to the state.
     */
    default public Translation2d translation2d()
    {
        return new Translation2d(getX(), getY());
    }

    @Override
    default Vertex2d vertex() {
        return new Vertex2d(getX(), getY());
    }

    /**
     * Returns another state with X and Y values from a vertex.
     * @param other The vertex to use.
     * @return A new state instance with X and Y values from the vertex.
     */
    public T copy(Vertex2d other);

    /**
     * Returns another state with X and Y values from a translation.
     * @param other The translation to use.
     * @return A new state instance with X and Y values from the translation.
     */
    public T copy(Translation2d other);
    
}
