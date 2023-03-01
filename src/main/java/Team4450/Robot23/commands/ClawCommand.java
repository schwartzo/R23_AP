package Team4450.Robot23.commands;

import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Claw;
import static Team4450.Robot23.Constants.*;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ClawCommand extends CommandBase {
    private final Claw claw;
    private final DoubleSupplier clawPower;
    
    public ClawCommand(Claw claw, DoubleSupplier clawPower) {
        Util.consoleLog();

        this.claw = claw;
        this.clawPower = clawPower;

        addRequirements(this.claw);
    }

    @Override
    public void execute() {
        setArmPower(clawPower);
    }

    @Override
    public void end(boolean interrupted) {
        Util.consoleLog("Interrupted status is ", interrupted);
    }

    private void setArmPower(DoubleSupplier inputPower) {
        claw.setPower(deadband(inputPower.getAsDouble(), THROTTLE_DEADBAND));
    }

    private static double deadband(double value, double deadband) {
        return Math.abs(value) > deadband ? value : 0.0;
    }
}