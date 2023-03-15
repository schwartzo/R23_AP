package Team4450.Robot23.pathfinder;

import edu.wpi.first.math.geometry.Translation2d;

public interface State2d<T, U> extends State<T, U>
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
    
}
