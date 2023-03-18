package Team4450.Robot23.pathfinder.math.astar;

import Team4450.Robot23.pathfinder.math.Vertex;

public class RouteVertex<V extends Vertex<V>> implements Comparable<RouteVertex<V>>
{

    private final V current;
    private V previous;

    private double rScore;
    private double eScore;

    public RouteVertex(V current)
    {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public RouteVertex(V current, V previous, double rScore, double eScore)
    {
        this.current = current;
        this.previous = previous;
        this.rScore = rScore;
        this.eScore = eScore;
    }

    public V getCurrent() {
        return current;
    }

    public V getPrevious() {
        return previous;
    }

    public double getEstimatedScore() {
        return eScore;
    }

    public double getRouteScore()
    {
        return rScore;
    }

    public void setPrevious(V previous) {
        this.previous = previous;
    }

    public void setRouteScore(double rScore) {
        this.rScore = rScore;
    }

    public void setEstimatedScore(double eScore) {
        this.eScore = eScore;
    }

    @Override
    public int compareTo(RouteVertex<V> arg0)
    {
        if (this.eScore > arg0.eScore)
        {
            return 1;
        }
        else if (this.eScore < arg0.eScore)
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
