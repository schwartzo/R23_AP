package Team4450.Robot23.pathfinder.math.astar;

import Team4450.Robot23.pathfinder.math.Vertex;

/**
 * Scorer which uses Euclidean distance only
 */
public class EuclideanDistanceScorer<V extends Vertex<V>> implements Scorer<V>
{

    @Override
    public double score(V from, V to)
    {
        return from.distance(to);
    }
}
