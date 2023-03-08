package Team4450.Robot23.commands;

import Team4450.Lib.SynchronousPID;
import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoWinch extends CommandBase {
    private final Winch winch;
    private double targetRevolutions;
    private SynchronousPID syncPID;
    private double Kp, Ki, Kd;
    private double tolerance;
    private double lastTimeCalled;

    private double maxPower = 0.5;

    public AutoWinch(Winch winch,
            double targetRevolutions,
            double tolerance) {
        this.winch = winch;
        this.targetRevolutions = targetRevolutions;
        this.tolerance = tolerance;

        addRequirements(this.winch);
    }

    @Override
    public void initialize() {
        Util.consoleLog();

        syncPID = new SynchronousPID(Kp, Ki, Kd);

        lastTimeCalled = Util.timeStamp();

        // The target revolutions was above the max position, and we don't want the arm
        // to move that far so we stop it right away
        if (targetRevolutions > winch.getMaxPosition())
            end(true);

        syncPID.reset();
        syncPID.setSetpoint(targetRevolutions);
        syncPID.setOutputRange(-maxPower, maxPower);

        SmartDashboard.putBoolean("AutoWinch", true);

    }

    @Override
    public void execute() {
        double time = Util.getElaspedTime(lastTimeCalled);
        lastTimeCalled = Util.timeStamp();

        winch.setPower(syncPID.calculate(winch.CURRENT_WINCH_ROTATIONS(), time));
    }

    @Override
    public boolean isFinished() {
        return syncPID.onTarget(tolerance) || winch.maxDownwardRotation();
    }

    @Override
    public void end(boolean interrupted) {
        winch.stopMotor();

        Util.consoleLog("Interrupted status is ", interrupted);

        SmartDashboard.putBoolean("AutoWinch", false);
    }
}
