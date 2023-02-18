package Team4450.Robot23.commands.autonomous;

import Team4450.Robot23.commands.AutoBalance;
import Team4450.Robot23.commands.AutoPositions;
import Team4450.Robot23.commands.AutoPositions.ClawStateNames;
import Team4450.Robot23.commands.AutoPositions.ComboStateNames;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.Pid;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.StopMotors;
import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.DriveBase;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ChargeStationAuto extends CommandBase {
    DriveBase drivebase;
    Arm arm;
    Winch winch;
    Claw claw;
    
    SequentialCommandGroup commands;
    Command command;

    public ChargeStationAuto(DriveBase drivebase, Arm arm, Winch winch, Claw claw) {
        this.drivebase = drivebase;

        addRequirements(this.drivebase);
        addRequirements(this.arm);
        addRequirements(this.winch);
        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        // Pre-command code for information
        
        
        // MOST OF THE VALUES ARE CURRENLY PLACEHOLDER
        // Rotate robot torwards target scoring position. Done before letting arm and winch move so it doesn't accidently run into anything.
        command = new AutoRotate(drivebase, 180);
        commands.addCommands(command);

        // Possibly need to move robot slightly forward? I do not know

        // Move set arm and winch up for target scoring position
        command = new AutoPositions(arm, winch, claw, ComboStateNames.HIGHEST_SCORING);
        commands.addCommands(command);

        // Drop the cone
        command = new AutoPositions(arm, winch, claw, null, null, ClawStateNames.FULLY_OPEN);
        commands.addCommands(command);

        // Exit the community zone
        command = new AutoDriveProfiled(drivebase, 0, StopMotors.stop, Brakes.on);
        commands.addCommands(command);

        // Move in front of the charging station
        command = new AutoStrafeProfiled(drivebase, 0, StopMotors.stop, Brakes.on);
        commands.addCommands(command);

        // Get on the charging station
        command = new AutoBalance(drivebase, -0.5);
        commands.addCommands(command);

        // Make it run
        commands.schedule();
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {

    }

    @Override
	public boolean isFinished() {
        return true;
    }
}
