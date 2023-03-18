package Team4450.Robot23.pathfinder.math;

public class Vertex2d extends Vertex<Vertex2d>
{

    private double x, y;

    public Vertex2d(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public Vertex2d polar(double distance, double rotation)
    {
        return new Vertex2d(x + distance * Math.cos(rotation), y + distance * Math.sin(rotation));
    }

    @Override
    public Vertex2d plus(Vertex2d other)
    {
        return new Vertex2d(x + other.getX(), y + other.getY());
    }

    @Override
    public Vertex2d minus(Vertex2d other)
    {
        return new Vertex2d(x + other.getX(), y + other.getY());
    }

    @Override
    public double dot(Vertex2d other)
    {
        return x * other.getX() + y * other.getY();
    }

    @Override
    public double distance(Vertex2d other)
    {
        return Math.hypot(x - other.getX(), y - other.getY());
    }
}
