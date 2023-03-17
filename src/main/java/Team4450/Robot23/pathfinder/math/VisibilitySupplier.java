package Team4450.Robot23.pathfinder.math;

@FunctionalInterface
public interface VisibilitySupplier<V extends Vertex>
{
    
    public boolean check(V a, V b);

}
