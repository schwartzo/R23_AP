package Team4450.Robot23.pathfinder;

import edu.wpi.first.wpilibj2.command.Command;

/**
 * Functional interface which creates a command using a type.
 */
@FunctionalInterface
public interface CommandSupplier<T>
{

    /**
     * Constructs the command.
     * @param arg Argument for command construction.
     * @return A new command constructed with argument.
     */
    public Command construct(T arg);

}