package Team4450.Robot23.pathfinder;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;

public class FieldMap {
    private List<FieldObstacle> obstacles = new ArrayList<>();

    /**
     * Base interface for obstacles
     */
    public static interface FieldObstacle {

        /**
         * Creates translations around an obstacle
         * @param path Original path which intersects the obstacle
         * @return A list of translations which will not intersect the obstacle
         */
        public List<Translation2d> path(Translation2d path);

        /**
         * Checks if a path intersects the obstacle
         * @param path Path to check
         * @return true if the path intersects the obstacle, false otherwise
         */
        public boolean check(Translation2d path);

    }

    /**
     * A polygonal obstacle
     */
    public static class PolygonObstacle implements FieldObstacle {

        private Translation2d origin;
        private List<Translation2d> sides;

        public PolygonObstacle(Translation2d origin, Translation2d... sides) {
            this.origin = origin;
            for (Translation2d t : sides)
                this.sides.add(t);
        }

        @Override
        public boolean check(Translation2d path) {
            List<Translation2d> lines = new ArrayList<>();
            return false;
        }

        @Override
        public List<Translation2d> path(Translation2d path) {
            // TODO Auto-generated method stub
            return null;
        }

        private boolean intersects(Translation2d a1, Translation2d a2, Translation2d b1, Translation2d b2) {
            return ccw(a1, b1, b2) != ccw(a2, b1, b2) && ccw(a1, a2, b1) != ccw(a1, a2, b2);
        }

        private boolean ccw(Translation2d a, Translation2d b, Translation2d c) {
            return (c.getY() - a.getY()) * (b.getX() - a.getX()) > (b.getY() - a.getY()) * (c.getX() - a.getX());
        }
    }
}
