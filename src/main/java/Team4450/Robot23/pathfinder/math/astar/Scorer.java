package Team4450.Robot23.pathfinder.math.astar;

import Team4450.Robot23.pathfinder.math.Vertex;

public interface Scorer<V extends Vertex<V>>
{
    
    public double score(V from, V to);

}
