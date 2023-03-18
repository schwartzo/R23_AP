package Team4450.Robot23.pathfinder.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all vertices.
 * V should be the type of the child class.
 */
public abstract class Vertex<V extends Vertex<V>>
{
    
    private List<V> neighbors = new ArrayList<>();

    /**
     * Adds a neighbor to the vertex.
     * @param v
     */
    public void addNeighbor(V v)
    {
        neighbors.add(v);
    }

    /**
     * Clears all neighbors.
     */
    public void clearNeighbors()
    {
        neighbors.clear();
    }

    /**
     * Returns the list of neighbors of the vertex.
     * @return The list of neighbors.
     */
    public List<V> getNeighbors()
    {
        return neighbors;
    }

    /**
     * Adds the coordinates of the vertex to another.
     * @param other The vertex to add.
     * @return A new vertex which is the sum of both operands.
     */
    public abstract V plus(V other);

    /**
     * Subtracts the coordinates of one vertex from another.
     * @param other The vertex whose coordinates should be subtracted.
     * @return A new vertex which is the difference between the two operands.
     */
    public abstract V minus(V other);

    /**
     * Computes the dot product of two vertices.
     * @param other The second vertex in the dot product computation.
     * @return The dot product of the two vertices.
     */
    public abstract double dot(V other);

    /**
     * Computes the distance between two vertices.
     * @param other The second vertex to use in the distance computation.
     * @return The Euclidean distance between the two vertices.
     */
    public abstract double distance(V other);
}
