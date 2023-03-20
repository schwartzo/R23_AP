package Team4450.Robot23.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Team4450.Lib.Util;
import Team4450.Robot23.pathfinder.math.Graph;
import Team4450.Robot23.pathfinder.math.MathConstants;
import Team4450.Robot23.pathfinder.math.Pathfinder;
import Team4450.Robot23.pathfinder.math.Vertex2d;
import Team4450.Robot23.pathfinder.math.astar.AStarPathfinder;
import Team4450.Robot23.pathfinder.math.astar.EuclideanDistanceScorer;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

/**
 * Obstacle and path logic on a 2d map.
 */
public class FieldMap2d
{

    private Map<String, FieldObstacleSet> obstacles = new HashMap<>();

    private Graph<Vertex2d> visGraph = new Graph<>((a, b) -> checkVisibility(a, b), () -> getNewVertices());

    /**
     * Adds a set of obstacles to the map.
     * @param name Name of the obstacle set.
     * @param obstacleSet Obstacle set to add.
     * @return The map instance.
     */
    public FieldMap2d addObstacleSet(String name, FieldObstacleSet obstacleSet)
    {
        obstacles.put(name, obstacleSet);
        visGraph.update(true);
        return this;
    }

    /**
     * Gets the obstacle map.
     * @return The map of obstacles used by the field map.
     */
    public Map<String, FieldObstacleSet> getObstacles()
    {
        return obstacles;
    }

    /**
     * Checks if one vertex is visible from another.
     * @param a The first vertex.
     * @param b The second vertex.
     * @return true if the path between the vertices is unobstructed, false otherwise.
     */
    public boolean checkVisibility(Vertex2d a, Vertex2d b)
    {
        Util.consoleLog("Checking " + a.getX() + "," + a.getY() + " to " + b.getX() + "," + b.getY());
        for (Map.Entry<String, FieldObstacleSet> obstacleSet : obstacles.entrySet())
        {
            if (!obstacleSet.getValue().isEnabled()) continue;
            for (FieldObstacle obstacle : obstacleSet.getValue().get())
            {
                if (obstacle.check(a, b)) return false;
            }
        }
        return true;
    }

    /**
     * Gets new vertices based on currently enabled field obstacles.
     * @return A new list with up-to-date vertices.
     */
    public List<Vertex2d> getNewVertices()
    {
        List<Vertex2d> vertices = new ArrayList<>();
        for (Map.Entry<String, FieldObstacleSet> obstacleSet : obstacles.entrySet())
        {
            if (!obstacleSet.getValue().isEnabled()) continue;
            for (FieldObstacle obstacle : obstacleSet.getValue().get())
            {
                obstacle.putVertices(vertices);
            }
        }
        return vertices;
    }

    /**
     * Computes a new path which avoids mapped obstacles.
     * @param original The unmodified path.
     * @return A path which will not intersect any mapped obstacle.
     */
    public <T extends State2d<T>> Path<T> computePath(Path<T> original)
    {
        Vertex2d start = original.start();
        Map<T, Vertex2d> points = new HashMap<>();
        Pathfinder<Vertex2d> p = new AStarPathfinder<>(visGraph, new EuclideanDistanceScorer<Vertex2d>(), new EuclideanDistanceScorer<Vertex2d>());
        Path.Builder<T> builder = new Path.Builder<T>(start, null).blankFrom(original);

        visGraph.pushState();
        visGraph.addVertex(start);
        T prev = null;
        for (T state : original)
        {
            Vertex2d v = new Vertex2d(
                    (prev == null ? original.start().getX() : prev.getX()) + state.getX(),
                    (prev == null ? original.start().getY() : prev.getY()) + state.getY());
            visGraph.addVertex(v);
            points.put(state, v);
            prev = state;
        }
        visGraph.update(false);
        for (Map.Entry<T, Vertex2d> point : points.entrySet())
        {
            List<Vertex2d> path = p.find(start, point.getValue());
            for (Vertex2d v : path)
            {
                builder.add(point.getKey().copy(v.minus(start)));
                start = v;
            }
        }
        visGraph.popState();
        return builder.remove(0).build();
    }

    public boolean isObstacleSetEnabled(String name)
    {
        return obstacles.get(name).isEnabled();
    }

    public void setObstacleSetEnabled(String name, boolean enabled)
    {
        obstacles.get(name).setEnabled(enabled);
        visGraph.update(true);
    }

    public void toggleObstacleSet(String name)
    {
        obstacles.get(name).toggle();
        visGraph.update(true);
    }

    /**
     * Base interface for obstacles.
     */
    public static interface FieldObstacle
    {

        /**
         * Checks if a path intersects the obstacle.
         * @param path The path to check.
         * @return true if the path intersects the obstacle, false otherwise.
         */
        public boolean check(Path<? extends State2d<?>> path);

        /**
         * Checks if a connection between two vertices would intersect the obstacle.
         * @param a The first vertex.
         * @param b The second vertex.
         * @return true if the connection would intersect the obstacle, false otherwise.
         */
        public boolean check(Vertex2d a, Vertex2d b);

        /**
         * Adds the vertices of the obstacle to a list.
         * @param list The list to add the vertices to.
         */
        public void putVertices(List<Vertex2d> list);

    }

    /**
     * A toggleable set of field obstacles.
     */
    public static class FieldObstacleSet
    {

        private boolean enabled;
        private final List<FieldObstacle> obstacles;

        /**
         * Constructs a FieldObjectSet from a list of obstacles.
         * @param obstacles List of obstacles to use.
         */
        public FieldObstacleSet(List<FieldObstacle> obstacles)
        {
            this.obstacles = obstacles;
        }

        /**
         * Constructs a FieldObjectSet from a list of obstacles.
         * @param obstacles List of obstacles to use.
         */
        public FieldObstacleSet(FieldObstacle... obstacles)
        {
            this.obstacles = new ArrayList<>();
            for (FieldObstacle obstacle : obstacles)
                this.obstacles.add(obstacle);
        }

        /**
         * Gets the underlying list of field obstacles.
         * @return The list of field obstacles used by the set.
         */
        public List<FieldObstacle> get()
        {
            return obstacles;
        }

        /**
         * Checks if the set is enabled.
         * @return true if enabled, false otherwise.
         */
        public boolean isEnabled()
        {
            return enabled;
        }

        /**
         * Enables or disabled the set.
         * @param enabled The new enabled value.
         */
        public void setEnabled(boolean enabled)
        {
            this.enabled = enabled;
        }

        /**
         * Toggles the set.
         */
        public void toggle()
        {
            enabled = enabled ? false : true;
        }

        /**
         * Builder for FieldObstacleSet.
         */
        public static class Builder
        {
            private boolean initallyEnabled;
            private List<FieldObstacle> obstacles = new ArrayList<>();

            /**
             * Add an obstacle.
             * @param obstacle Obstacle to add.
             * @return Updated builder instance.
             */
            public Builder add(FieldObstacle obstacle)
            {
                obstacles.add(obstacle);
                return this;
            }

            /**
             * Enable this set when it is initially added to a map.
             * @return Updated builder instance.
             */
            public Builder enabled()
            {
                initallyEnabled = true;
                return this;
            }

            /**
             * Disable this set when it is initially added to a map.
             * @return Updated builder instance.
             */
            public Builder disabled()
            {
                initallyEnabled = false;
                return this;
            }

            /**
             * Builds the FieldObstacleSet.
             * @return New FieldObstacleSet instance with specified parameters.
             */
            public FieldObstacleSet build()
            {
                FieldObstacleSet result = new FieldObstacleSet(obstacles);
                result.setEnabled(initallyEnabled);
                return result;
            } 
        }
    }

    /**
     * A polygonal obstacle.
     */
    public static class PolygonObstacle implements FieldObstacle
    {

        private List<Vertex2d> vertices = new ArrayList<>();

        /**
         * Instantiates a new polygon obstacle from a list of vertices.
         * @param vertices The vertices to use for the obstacle.
         */
        public PolygonObstacle(Vertex2d... vertices)
        {
            for (Vertex2d v : vertices)
                this.vertices.add(v);
        }

        /**
         * Instantiates a rectangular PolygonObstacle.
         * @param origin Pose2d of bottom left corner of obstacle (assuming a rotation of 0 degrees).
         * @param x Width of the obstacle (assuming no rotation).
         * @param y Height of the obstacle (assuming no rotation).
         * @return A new rectangular PolygonObstacle.
         */
        public static PolygonObstacle rect(Pose2d origin, double x, double y)
        {
            Vertex2d v = new Vertex2d(origin.getTranslation().getX(), origin.getTranslation().getY());
            return new PolygonObstacle(v, v.polarOffset(x, origin.getRotation().getRadians()),
                    v.polarOffset(Math.hypot(x, y), Rotation2d.fromRadians(Math.atan(y / x)).plus(origin.getRotation()).getRadians()),
                    v.polarOffset(y, Rotation2d.fromDegrees(90).plus(origin.getRotation()).getRadians()));
        }

        @Override
        public void putVertices(List<Vertex2d> list)
        {
            list.addAll(vertices);
        }

        @Override
        public boolean check(Path<? extends State2d<?>> path)
        {
            int vcon = 0;
            Vertex2d prev = vertices.get(vertices.size() - 1);
            for (Vertex2d v : vertices)
            {
                if (erange(0, intersection(path.start(), path.get(0).vertex(), prev, v), 1)) return true;
                if (irange(0, intersection(path.start(), path.get(0).vertex(), prev, v), 1)) vcon++;
                if (intersection(path.start(), path.get(0).vertex(), prev, v) == Double.NaN) vcon--;
                prev = v;
            }
            return vcon > 2;
        }

        @Override
        public boolean check(Vertex2d a, Vertex2d b)
        {
            int vcon = 0;
            Vertex2d prev = vertices.get(vertices.size() - 1);
            for (Vertex2d v : vertices)
            {
                if (erange(0, intersection(new Vertex2d(a.getX(), a.getY()), new Vertex2d(b.getX(), b.getY()), prev, v), 1))
                {
                    Util.consoleLog("Blocking " + v.getX() + "," + v.getY() + " [" + intersection(new Vertex2d(a.getX(), a.getY()), new Vertex2d(b.getX(), b.getY()), prev, v) + "]");
                    return true;
                }
                if (irange(0, intersection(new Vertex2d(a.getX(), a.getY()), new Vertex2d(b.getX(), b.getY()), prev, v), 1)) vcon++;
                if (intersection(new Vertex2d(a.getX(), a.getY()), new Vertex2d(b.getX(), b.getY()), prev, v) == Double.NaN) vcon--;
                prev = v;
            }
            return vcon > 2;
        }

        private double intersection(Vertex2d A, Vertex2d B, Vertex2d C, Vertex2d D)
        {
            double[] h = {h(A, B, C, D), h(C, D, A, B)};
            if (irange(0, h[0], 1) && irange(0, h[1], 1)) return h[0];
            return Math.abs(h[0]) > Math.abs(h[1]) ? h[0] : h[1];
        }

        private double h(Vertex2d A, Vertex2d B, Vertex2d C, Vertex2d D)
        {
            Vertex2d E = B.minus(A);
            Vertex2d F = D.minus(C);
            Vertex2d P = new Vertex2d(-E.getY(), E.getX());
            return F.dot(P) == 0 ? Double.NaN : A.minus(C).dot(P) / F.dot(P);
        }

        private boolean erange(double min, double x, double max)
        {
            if (Math.abs(max - x) <= MathConstants.EPSILON || Math.abs(min - x) <= MathConstants.EPSILON) return false;
            return x > min && x < max;
        }

        private boolean irange(double min, double x, double max)
        {
            if (Math.abs(max - x) <= MathConstants.EPSILON || Math.abs(min - x) <= MathConstants.EPSILON) return true;
            return x >= min && x <= max;
        }
    }
}
