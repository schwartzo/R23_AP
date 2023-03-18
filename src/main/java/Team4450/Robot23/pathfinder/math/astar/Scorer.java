package Team4450.Robot23.pathfinder.math.astar;

import Team4450.Robot23.pathfinder.math.Vertex;

/**
 * Base interface for scorers.
 */
public interface Scorer<V extends Vertex<V>>
{
    
    /**
     * Computes the score of a connection between two vertices.
     * @param from Starting vertex.
     * @param to Ending vertex.
     * @return The score of the connection.
     */
    public double score(V from, V to);

}
