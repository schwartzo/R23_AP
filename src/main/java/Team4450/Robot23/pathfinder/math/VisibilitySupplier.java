package Team4450.Robot23.pathfinder.math;

/**
 * Functional interface which takes two vertices and determines visibility.
 */
@FunctionalInterface
public interface VisibilitySupplier<V extends Vertex<V>>
{
    
    /**
     * Checks if the two vertices are visible to each other.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return true if the vertices are visible to each other, false otherwise.
     */
    public boolean check(V a, V b);

}
