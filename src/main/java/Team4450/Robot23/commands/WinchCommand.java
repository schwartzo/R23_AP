package Team4450.Robot23.commands;

import Team4450.Lib.Util;
import Team4450.Robot23.subsystems.Winch;
import static Team4450.Robot23.Constants.*;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class WinchCommand extends CommandBase {
    private final Winch winch;
    private final double winchPower;
    
    public WinchCommand(Winch winch, double winchPower) {
        Util.consoleLog();

        this.winch = winch;
        this.winchPower = winchPower;

        addRequirements(this.winch);
    }

    @Override
    public void execute() {
        setWinchPower(winchPower);
    }

    @Override
    public void end(boolean interrupted) {
        Util.consoleLog("Interrupted status is ", interrupted);
    }

    private void setWinchPower(double inputPower) {
        winch.setPower(deadband(inputPower, THROTTLE_DEADBAND));
    }

    private static double deadband(double value, double deadband) {
        return Math.abs(value) > deadband ? value : 0.0;
    }
}