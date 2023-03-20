package Team4450.Robot23.pathfinder.math;

import java.util.List;

/**
 * Base pathfinder interface.
 */
public interface Pathfinder<V extends Vertex<V>>
{

    /**
     * Finds a path between two vertices.
     * @param from Starting vertex.
     * @param to Ending vertex.
     * @return A list of vertices computed by the pathfinding algorithm.
     */
    public List<V> find(V from, V to);

}
