package Team4450.Robot23.commands;

import Team4450.Lib.SynchronousPID;
import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Arm;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoArm extends CommandBase {
    private final Arm arm;
    private double targetRevolutions;
    private SynchronousPID syncPID;
    private double tolerance;
    private double lastTimeCalled;

    private double maxPower = 0.5;

    public AutoArm(Arm arm,
            double targetRevolutions,
            double tolerance) {
        this.arm = arm;
        this.targetRevolutions = targetRevolutions;
        this.tolerance = tolerance;

        addRequirements(this.arm);
    }

    @Override
    public void initialize() {
        Util.consoleLog();

        lastTimeCalled = Util.timeStamp();

        // The target revolutions was above the max position, and we don't want the arm
        // to move that far so we stop it right away
        if (targetRevolutions > arm.getMaxPosition())
            end(true);

        syncPID.reset();
        syncPID.setSetpoint(targetRevolutions);
        syncPID.setOutputRange(-maxPower, maxPower);

        SmartDashboard.putBoolean("AutoArm", true);

    }

    @Override
    public void execute() {
        double time = Util.getElaspedTime(lastTimeCalled);
        lastTimeCalled = Util.timeStamp();

        arm.setPower(syncPID.calculate(arm.CURRENT_ARM_ROTATIONS(), time));
    }

    @Override
    public boolean isFinished() {
        return syncPID.onTarget(tolerance) || arm.fullyRetracted();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stopMotor();

        Util.consoleLog("Interrupted status is ", interrupted);

        SmartDashboard.putBoolean("AutoArm", false);
    }
}
