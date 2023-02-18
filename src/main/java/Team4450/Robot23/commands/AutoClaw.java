package Team4450.Robot23.commands;

import Team4450.Lib.SynchronousPID;
import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Claw;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoClaw extends CommandBase {
    private final Claw claw;
    private double targetRevolutions;
    private SynchronousPID syncPID;
    private double tolerance;
    private double lastTimeCalled;

    private double maxPower = 0.5;

    public AutoClaw(Claw claw,
            double targetRevolutions,
            double tolerance) {
        this.claw = claw;
        this.targetRevolutions = targetRevolutions;
        this.tolerance = tolerance;

        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        Util.consoleLog();

        lastTimeCalled = Util.timeStamp();

        // The target revolutions was above the max position, and we don't want the arm
        // to move that far so we stop it right away
        if (targetRevolutions > claw.getMaxEncoder())
            end(true);

        syncPID.reset();
        syncPID.setSetpoint(targetRevolutions);
        syncPID.setOutputRange(-maxPower, maxPower);

        SmartDashboard.putBoolean("AutoClaw", true);

    }

    @Override
    public void execute() {
        double time = Util.getElaspedTime(lastTimeCalled);
        lastTimeCalled = Util.timeStamp();

        claw.setPower(syncPID.calculate(claw.CURRENT_CLAW_ENCODER_COUNT(), time));
    }

    @Override
    public boolean isFinished() {
        return syncPID.onTarget(tolerance) || claw.fullyRetracted();
    }

    @Override
    public void end(boolean interrupted) {
        claw.stopMotor();

        Util.consoleLog("Interrupted status is ", interrupted);

        SmartDashboard.putBoolean("AutoClaw", false);
    }
}
