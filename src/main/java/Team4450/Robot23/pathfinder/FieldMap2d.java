package Team4450.Robot23.pathfinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/**
 * Obstacle and path logic on a 2d map.
 */
public class FieldMap2d
{

    private Map<String, FieldObstacleSet> obstacles = new HashMap<>();

    /**
     * Adds a set of obstacles to the map.
     * @param name Name of the obstacle set.
     * @param obstacleSet Obstacle set to add.
     */
    public void addObstacleSet(String name, FieldObstacleSet obstacleSet)
    {
        obstacles.put(name, obstacleSet);
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
     * Computes a new path which avoids mapped obstacles.
     * @param original The unmodified path.
     * @return A path which will not intersect any mapped obstacle.
     */
    public <T extends State2d<T, ?>> Path<T> computePath(Path<T> original)
    {
        Path<T> path = original;
        Translation2d prev = original.start();
        for (int i = 0; i < path.size(); i++)
        {
            for (Entry<String, FieldObstacleSet> obstacleSet : obstacles.entrySet())
            {
                if (!obstacleSet.getValue().isEnabled()) continue;
                for (FieldObstacle obstacle : obstacleSet.getValue().get())
                {
                    if (!obstacle.check(new Path.Builder<T>(prev, path.get(i)).build())) continue;
                    path.remove(i);
                    path.addAll(i, obstacle.path(new Path.Builder<T>(prev, path.get(i)).build()));
                }
            }
            prev = path.get(i - 1).translation2d();
        }
        return path;
    }

    public boolean isObstacleSetEnabled(String name)
    {
        return obstacles.get(name).isEnabled();
    }

    public void setObstacleSetEnabled(String name, boolean enabled)
    {
        obstacles.get(name).setEnabled(enabled);
    }

    public void toggleObstacleSet(String name)
    {
        obstacles.get(name).toggle();
    }

    /**
     * Base interface for obstacles.
     */
    public static interface FieldObstacle
    {

        /**
         * Creates path around an obstacle.
         * @param original The unmodified path.
         * @return A series of translations (first from the supplied start) which will not intersect the obstacle.
         */
        public <T extends State2d<T, ?>> Path<T> path(Path<T> original);

        /**
         * Checks if a path intersects the obstacle.
         * @param path The path to check.
         * @return true if the path intersects the obstacle, false otherwise.
         */
        public boolean check(Path<? extends State2d<?, ?>> path);

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
            private List<FieldObstacle> obstacles;

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

        private Translation2d origin;
        private List<Translation2d> sides;

        /**
         * Instantiates a PolygonObstacle. Connection to origin is automatic.
         * @param origin Origin of first side.
         * @param sides Translation from end of last side to end of next side.
         */
        public PolygonObstacle(Translation2d origin, Translation2d... sides)
        {
            this.origin = origin;
            for (Translation2d t : sides)
                this.sides.add(t);
            this.sides.add(origin);
        }

        /**
         * Instantiates a rectangular PolygonObstacle.
         * @param origin Pose2d of top left corner of obstacle (assuming a rotation of 0 degrees).
         * @param size Translation from top left corner to bottom right corner of obstacle (assuming 0 degrees rotation).
         * @return A new rectangular PolygonObstacle.
         */
        public PolygonObstacle rect(Pose2d origin, Translation2d size)
        {
            return new PolygonObstacle(origin.getTranslation(),
                    new Translation2d(size.getX(), origin.getRotation()),
                    new Translation2d(size.getY(), Rotation2d.fromDegrees(90).minus(origin.getRotation())),
                    new Translation2d(size.getX(), Rotation2d.fromDegrees(180).plus(origin.getRotation())));
        }

        @Override
        public boolean check(Path<? extends State2d<?, ?>> path)
        {
            Translation2d prev = origin;
            for (Translation2d side : sides)
            {
                if (range(0, intersection(path.start(), path.get(0).translation2d(), prev, side), 1)) return true;
                prev = side;
            }
            return false;
        }

        @Override
        public <T extends State2d<T, ?>> Path<T> path(Path<T> original)
        {
            Path<T> path = original;
            Translation2d prev = origin;
            for (Translation2d side : sides)
            {
                if (range(0, intersection(path.start(), path.get(0).translation2d(), prev, side), 1))
                {
                    path.add(intersection(path.start(), path.get(0).translation2d(), prev, side) < 0.5 ? path.get(0).copy(prev).get() : path.get(0).copy(side).get());
                    break;
                }
                prev = side;
            }
            prev = origin;
            for (int i = 0; i < path.size(); i++)
            {
                if (check(new Path.Builder<T>(prev, path.get(i)).build()))
                {
                    path.remove(i);
                    path.addAll(i, path(new Path.Builder<T>(prev, path.get(i)).build()));
                }
                prev = path.get(i).translation2d();
            }
            return path;
        }

        private double intersection(Translation2d A, Translation2d B, Translation2d C, Translation2d D)
        {
            Translation2d E = A.minus(B);
            Translation2d F = D.minus(C);
            Translation2d P = new Translation2d(-E.getY(), E.getX());
            return dot(F, P) == 0 ? 0 : dot((A.minus(C)), P) / dot(F, P);
        }

        private double dot(Translation2d a, Translation2d b)
        {
            return a.getX() * b.getX() + a.getY() * b.getY();
        }

        private boolean range(double min, double x, double max)
        {
            return x > min && x < max ? true : false;
        }
    }
}
