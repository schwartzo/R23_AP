package Team4450.Robot23.pathfinder;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;

/**
 * Functional interface which creates a command from a translation.
 */
@FunctionalInterface
public interface TranslationCommand {
    
    /**
     * Instantiates a command based on a translation.
     * @param t Translation to use.
     * @return Command constructed with specified translation.
     */
    Command construct(Translation2d t);

}
