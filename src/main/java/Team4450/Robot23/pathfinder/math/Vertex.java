package Team4450.Robot23.pathfinder.math;

import java.util.ArrayList;
import java.util.List;

public class Vertex
{
    
    private List<Vertex> neighbors = new ArrayList<>();

    public void addNeighbor(Vertex v)
    {
        neighbors.add(v);
    }

    public void clearNeighbors()
    {
        neighbors.clear();
    }

    public List<Vertex> getNeighbors()
    {
        return neighbors;
    }
}
