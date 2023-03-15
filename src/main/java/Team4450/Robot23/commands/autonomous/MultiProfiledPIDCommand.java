package Team4450.Robot23.commands.autonomous;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * A command that contains multiple profiled PID controllers, measurement and goal
 * suppliers, and output consumers
 */
public class MultiProfiledPIDCommand extends CommandBase
{

    /**
     * Container class for PID parameters
     */
    public static class PID
    {
        public final ProfiledPIDController controller;
        public Supplier<Double> measurement;
        public Supplier<State> goal;
        public BiConsumer<Double, State> output;

        public PID(ProfiledPIDController controller, Supplier<Double> measurement, Supplier<State> goal,
                BiConsumer<Double, State> output)
        {
            this.controller = controller;
            this.measurement = measurement;
            this.goal = goal;
            this.output = output;
        }
    }

    protected Map<String, PID> controllers = new HashMap<>();

    /**
     * Adds a new PID to list.
     * 
     * @param p PID to add.
     */
    protected void addPID(String name, PID p)
    {
        controllers.put(name, p);
    }

    /**
     * Constructs and adds a new PID to the list.
     * 
     * @param controller  ProfiledPIDController for the new PID.
     * @param measurement Measurement supplier.
     * @param goal        Goal state.
     * @param output      Output consumer.
     */
    protected void addPID(String name, ProfiledPIDController controller, Supplier<Double> measurement, Supplier<State> goal,
            BiConsumer<Double, State> output)
    {
        controllers.put(name, new PID(controller, measurement, goal, output));
    }

    protected PID getPID(String name)
    {
        return controllers.get(name);
    }

    protected Supplier<State> stateSupplier(Supplier<Double> s)
    {
        return () -> new State(s.get(), 0.0);
    }

    @Override
    public void initialize()
    {
        for (Map.Entry<String, PID> p : controllers.entrySet())
        {
            p.getValue().controller.reset(p.getValue().measurement.get());
        }
    }

    @Override
    public void execute()
    {
        for (Map.Entry<String, PID> p : controllers.entrySet())
        {
            p.getValue().output.accept(p.getValue().controller.calculate(p.getValue().measurement.get(),
                    p.getValue().goal.get()), p.getValue().controller.getSetpoint());
        }
    }

    @Override
    public void end(boolean interrupted)
    {
        for (Map.Entry<String, PID> p : controllers.entrySet())
        {
            p.getValue().output.accept(0.0, new State());
        }
    }
}
