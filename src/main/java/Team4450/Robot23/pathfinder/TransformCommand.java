package Team4450.Robot23.pathfinder;

import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Functional interface which creates a command from a transform.
 */
@FunctionalInterface
public interface TransformCommand {
    
    /**
     * Instantiates a command based on a transform.
     * @param t Transform to use.
     * @return Command constructed with specified transform.
     */
    Command construct(Transform2d t);

}
