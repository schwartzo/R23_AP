package Team4450.Robot23.pathfinder.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Supplier;

/**
 * A directed graph.
 */
public class Graph<V extends Vertex<V>>
{
    
    private List<V> vertices = new ArrayList<>();

    private Stack<List<V>> states = new Stack<>();

    VisibilitySupplier<V> visibilitySupplier;
    Supplier<List<V>> vertexSupplier;

    /**
     * Instantiates a graph with a visibility supplier and a vertex supplier.
     * @param visibilitySupplier The visibility supplier.
     * @param vertexSupplier The vertex supplier.
     */
    public Graph(VisibilitySupplier<V> visibilitySupplier, Supplier<List<V>> vertexSupplier)
    {
        this.visibilitySupplier = visibilitySupplier;
        this.vertexSupplier = vertexSupplier;
    }

    /**
     * Updates the list of vertices and their neighbor lists.
     * @param refreshVertices Gets a new list of vertices if true.
     */
    public void update(boolean refreshVertices)
    {
        vertices = vertexSupplier.get();
        for (V a : vertices)
        {
            a.clearNeighbors();
            for (V b : vertices)
            {
                if (visibilitySupplier.check(a, b))
                    a.addNeighbor(b);
            }
        }
    }

    /**
     * Adds a vertex to the graph.
     * @param v The vertex to add.
     */
    public void addVertex(V v)
    {
        vertices.add(v);
    }

    /**
     * Gets the connections to a vertex.
     * @param v The vertex to find the connections to.
     * @return The list of connections from the vertex.
     */
    public List<V> getVertexConnections(V v)
    {
        if (vertices.contains(v))
            return vertices.get(vertices.indexOf(v)).getNeighbors();
        return new ArrayList<V>();
    }

    /**
     * Clears the vertices.
     */
    public void clearVertices()
    {
        vertices.clear();
    }

    /**
     * Pushes the current vertices to the states stack.
     */
    public void pushState()
    {
        states.push(new ArrayList<V>(vertices));
    }

    /**
     * Pops the vertices from the states stack.
     */
    public void popState()
    {
        vertices = states.pop();
    }
}
