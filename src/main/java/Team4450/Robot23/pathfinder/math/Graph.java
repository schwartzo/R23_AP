package Team4450.Robot23.pathfinder.math;

import java.util.ArrayList;
import java.util.List;

public class Graph<V extends Vertex>
{
    
    private List<V> vertices = new ArrayList<>();

    VisibilitySupplier<V> checkVisibility;

    public Graph(VisibilitySupplier<V> checkVisibility)
    {
        this.checkVisibility = checkVisibility;
    }

    public void update()
    {
        for (V a : vertices)
        {
            a.clearNeighbors();
            for (V b : vertices)
            {
                if (checkVisibility.check(a, b))
                    a.addNeighbor(b);
            }
        }
    }

    public void addVertex(V v)
    {
        vertices.add(v);
    }

    public void clearVertices()
    {
        vertices.clear();
    }
}
