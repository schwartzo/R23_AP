package Team4450.Robot23.commands;

import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Arm;
import static Team4450.Robot23.Constants.*;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class ArmCommand extends CommandBase {
    private final Arm arm;
    private final DoubleSupplier armPower;
    
    public ArmCommand(Arm arm, DoubleSupplier armPower) {
        Util.consoleLog();

        this.arm = arm;
        this.armPower = armPower;

        addRequirements(this.arm);
    }

    @Override
    public void execute() {
        setArmPower(armPower);
    }

    @Override
    public void end(boolean interrupted) {
        Util.consoleLog("Interrupted status is ", interrupted);
    }

    private void setArmPower(DoubleSupplier inputPower) {
        arm.setPower(deadband(inputPower.getAsDouble(), THROTTLE_DEADBAND));
    }

    private static double deadband(double value, double deadband) {
        return Math.abs(value) > deadband ? value : 0.0;
    }
}