package Team4450.Robot23.pathfinder.math.astar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import Team4450.Robot23.pathfinder.math.Graph;
import Team4450.Robot23.pathfinder.math.Pathfinder;
import Team4450.Robot23.pathfinder.math.Vertex;

/**
 * A* pathfinder implementation.
 */
public class AStarPathfinder<V extends Vertex<V>> implements Pathfinder<V>
{
    
    private final Graph<V> graph;
    private final Scorer<V> vertexScorer;
    private final Scorer<V> targetScorer;

    /**
     * Instantiates an A* pathfinder.
     * @param graph Graph to use for pathfinding.
     * @param vertexScorer Vertex connection scorer.
     * @param targetScorer Path scorer.
     */
    public AStarPathfinder(Graph<V> graph, Scorer<V> vertexScorer, Scorer<V> targetScorer)
    {
        this.graph = graph;
        this.vertexScorer = vertexScorer;
        this.targetScorer = targetScorer;
    }

    @Override
    public List<V> find(V from, V to)
    {
        Map<V, RouteVertex<V>> rMap = new HashMap<>();
        Queue<RouteVertex<V>> set = new PriorityQueue<>();

        RouteVertex<V> start = new RouteVertex<>(from, null, 0.0, targetScorer.score(from, to));
        set.add(start);
        rMap.put(from, start);

        while (!set.isEmpty())
        {
            RouteVertex<V> next = set.poll();
            if (next.getCurrent().equals(to))
            {
                List<V> route = new ArrayList<>();
                RouteVertex<V> current = next;
                do
                {
                    route.add(0, current.getCurrent());
                    current = rMap.get(current.getPrevious());
                }
                while (current != null);
                return route;
            }

            for (V v : graph.getVertexConnections(next.getCurrent()))
            {
                RouteVertex<V> nextVertex = rMap.getOrDefault(v, new RouteVertex<>(v));
                double newScore = next.getRouteScore() + vertexScorer.score(next.getCurrent(), v);

                if (newScore < nextVertex.getRouteScore())
                {
                    nextVertex.setPrevious(next.getCurrent());
                    nextVertex.setRouteScore(newScore);
                    nextVertex.setEstimatedScore(newScore + targetScorer.score(v, to));
                    set.add(nextVertex);
                }
            }
        }

        throw new IllegalStateException("pathfinder: no route found");
    }
}
