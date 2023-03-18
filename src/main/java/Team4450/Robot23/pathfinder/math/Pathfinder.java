package Team4450.Robot23.pathfinder.math;

import java.util.List;

public interface Pathfinder<V extends Vertex<V>>
{

    public List<V> find(V from, V to);

}
