package Team4450.Robot23.pathfinder;

import edu.wpi.first.math.geometry.Translation2d;

/**
 * State for use with paths.
 * T should be the type of the implementor.
 */
public interface State<T, U>
{
    
    /**
     * Gets the base of the state (e.g. a translation or a transform).
     * @return The object that the state is based on.
     */
    public U base();

    /**
     * Gets the implementor instance that this method is called on.
     * @return The implementor instance.
     */
    public T get();

    /**
     * Adds two states (used in calculations).
     * @param other The other state to add.
     * @return A new state which is the result of an addition.
     */
    public State<T, U> plus(State<T, U> other);

    /**
     * Subtracts two states.
     * @param other The other state which will be subtracted.
     * @return A new state which is the result of a subtraction.
     */
    public State<T, U> minus(State<T, U> other);

    /**
     * Returns another state which should have the same X and Y values,
     * other information may not be preserved. This can be used so that only
     * one state contains certain data after path computation creates more states.
     * @return A new state instance which has the same X and Y values.
     */
    public State<T, U> copy();

    /**
     * Returns another state with X and Y values from a translation.
     * @param modify The translation to use.
     * @return A new state instance with X and Y values from the translation.
     */
    public State<T, U> copy(Translation2d modify);

}