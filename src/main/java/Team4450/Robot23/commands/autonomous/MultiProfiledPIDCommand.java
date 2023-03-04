package Team4450.Robot23.commands.autonomous;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;

public class MultiProfiledPIDCommand {

    public static class PID {
        public final ProfiledPIDController controller;
        public Supplier<Double> measurement;
        public Supplier<State> goal;
        public BiConsumer<Double, State> output;

        public PID(ProfiledPIDController controller, Supplier<Double> measurement, Supplier<State> goal,
                BiConsumer<Double, State> output) {
            this.controller = controller;
            this.measurement = measurement;
            this.goal = goal;
            this.output = output;
        }
    }

    protected List<PID> controllers = new ArrayList<>();

    /**
     * Adds a new PID to list.
     * 
     * @param p PID to add.
     */
    protected void addPID(PID p) {
        controllers.add(p);
    }

    /**
     * Constructs and adds a new PID to the list.
     * 
     * @param controller  ProfiledPIDController for the new PID.
     * @param measurement Measurement supplier.
     * @param goal        Goal state.
     * @param output      Output consumer.
     */
    protected void addPID(ProfiledPIDController controller, Supplier<Double> measurement, Supplier<State> goal,
            BiConsumer<Double, State> output) {
        controllers.add(new PID(controller, measurement, goal, output));
    }

    protected void execute() {
        for (PID p : controllers) {
            p.output.accept(p.controller.calculate(p.measurement.get(), p.goal.get()),
                    p.controller.getSetpoint());
        }
    }
}
