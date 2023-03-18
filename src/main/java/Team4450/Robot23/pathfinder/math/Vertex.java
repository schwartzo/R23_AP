package Team4450.Robot23.pathfinder.math;

import java.util.ArrayList;
import java.util.List;

public abstract class Vertex<V extends Vertex<V>>
{
    
    private List<V> neighbors = new ArrayList<>();

    public void addNeighbor(V v)
    {
        neighbors.add(v);
    }

    public void clearNeighbors()
    {
        neighbors.clear();
    }

    public List<V> getNeighbors()
    {
        return neighbors;
    }

    public abstract V plus(V other);

    public abstract V minus(V other);

    public abstract double dot(V other);

    public abstract double distance(V other);
}
