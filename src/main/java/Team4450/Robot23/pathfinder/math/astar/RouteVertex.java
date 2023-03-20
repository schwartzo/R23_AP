package Team4450.Robot23.pathfinder.math.astar;

import Team4450.Robot23.pathfinder.math.Vertex;

/**
 * A vertex which is part of the route.
 */
public class RouteVertex<V extends Vertex<V>> implements Comparable<RouteVertex<V>>
{

    private final V current;
    private V previous;

    private double rScore;
    private double eScore;

    /**
     * Instantiates a route vertex.
     * @param current Current vertex.
     */
    public RouteVertex(V current)
    {
        this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Instantiates a route vertex.
     * @param current Current vertex.
     * @param previous Previous vertex.
     * @param rScore Route score.
     * @param eScore Estimated score.
     */
    public RouteVertex(V current, V previous, double rScore, double eScore)
    {
        this.current = current;
        this.previous = previous;
        this.rScore = rScore;
        this.eScore = eScore;
    }

    /**
     * Returns current vertex.
     * @return Current vertex.
     */
    public V getCurrent()
    {
        return current;
    }

    /**
     * Returns the previous vertex in the route.
     * @return The previous vertex.
     */
    public V getPrevious()
    {
        return previous;
    }

    /**
     * Returns the estimated score.
     * @return The estimated score.
     */
    public double getEstimatedScore()
    {
        return eScore;
    }

    /**
     * Returns the route score.
     * @return The route score.
     */
    public double getRouteScore()
    {
        return rScore;
    }

    /**
     * Sets the previous vertex.
     * @param previous The previous vertex.
     */
    public void setPrevious(V previous)
    {
        this.previous = previous;
    }

    /**
     * Sets the route score.
     * @param rScore The route score.
     */
    public void setRouteScore(double rScore)
    {
        this.rScore = rScore;
    }

    /**
     * Sets the estimated score.
     * @param eScore The estimated score.
     */
    public void setEstimatedScore(double eScore)
    {
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
