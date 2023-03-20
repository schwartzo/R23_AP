package Team4450.Robot23.pathfinder;

import Team4450.Robot23.pathfinder.math.Vertex;

/**
 * State for use with paths.
 * T should be the type of the implementor.
 */
public interface State<T>
{

    /**
     * Adds two states (used in calculations).
     * @param other The other state to add.
     * @return A new state which is the result of an addition.
     */
    public T plus(T other);

    /**
     * Subtracts two states.
     * @param other The other state which will be subtracted.
     * @return A new state which is the result of a subtraction.
     */
    public T minus(T other);

    /**
     * Returns another state which should have the same position values,
     * other information may not be preserved. This can be used so that only
     * one state contains certain data after path computation creates more states.
     * @return A new state instance which has the same position values.
     */
    public T copy();

    /**
     * Returns a vertex representing the position of the state.
     * @return A vertex representing the position of the state.
     */
    public Vertex<? extends Vertex<?>> vertex();

}