package Team4450.Robot23.commands.autonomous;

import Team4450.Robot23.commands.AutoBalance;
import Team4450.Robot23.commands.AutoPositions;
import Team4450.Robot23.commands.AutoPositions.ClawStateNames;
import Team4450.Robot23.commands.AutoPositions.ComboStateNames;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.StopMotors;
import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.DriveBase;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class DoubleConeAuto extends CommandBase {
    DriveBase driveBase;
    Arm arm;
    Winch winch;
    Claw claw;
    
    SequentialCommandGroup commands;
    Command command;

    public DoubleConeAuto(DriveBase driveBase, Arm arm, Winch winch, Claw claw) {
        this.driveBase = driveBase;

        addRequirements(this.driveBase);
        addRequirements(this.arm);
        addRequirements(this.winch);
        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        // Pre-command code for information
        
        
        // Rotate robot torwards target scoring position. Done before letting arm and winch move so it doesn't accidently run into anything.
        command = new AutoRotate(driveBase, 180);
        commands.addCommands(command);

        // Move set arm and winch up for target scoring position
        command = new AutoPositions(arm, winch, claw, ComboStateNames.HIGHEST_SCORING);
        commands.addCommands(command);

        // Drop the cone
        command = new AutoPositions(arm, winch, claw, null, null, ClawStateNames.FULLY_OPEN);
        commands.addCommands(command);

        // Pull in the arm
        command = new AutoPositions(arm, winch, claw, ComboStateNames.FULLY_CONTAINED);
        commands.addCommands(command);

        // Exit the community zone to go get another cone (going less than the actual distance because the arm reaches out)
        command = new AutoDriveProfiled(driveBase, 5.1, StopMotors.stop, Brakes.on);
        commands.addCommands(command);

        // Rotate again so you are facing torwards the cone
        command = new AutoRotate(driveBase, 180);
        commands.addCommands(command);

        // Prepare to pickup the cone
        command = new AutoPositions(arm, winch, claw, ComboStateNames.OBJECT_PICKUP);
        commands.addCommands(command);

        // Pickup the cone
        command = new AutoPositions(arm, winch, claw, null, null, ClawStateNames.HOLDING_CONE);

        // Rotate again to face torwards the placement location
        command = new AutoRotate(driveBase, 180);
        commands.addCommands(command);

        // Move to the cone placement location
        command = new AutoDriveProfiled(driveBase, -5.1, StopMotors.stop, Brakes.on);
        commands.addCommands(command);

        // Move slightly to the right to line it up with a new cone placement pole
        command = new AutoStrafeProfiled(driveBase, -1.11, StopMotors.stop, Brakes.on);
        commands.addCommands(command);

        // Move set arm and winch up for target scoring position
        command = new AutoPositions(arm, winch, claw, ComboStateNames.HIGHEST_SCORING);
        commands.addCommands(command);

        // Drop the cone
        command = new AutoPositions(arm, winch, claw, null, null, ClawStateNames.FULLY_OPEN);
        commands.addCommands(command);        

        // Make it run
        commands.schedule();
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {
        driveBase.drive(0, 0, 0);
    }

    @Override
	public boolean isFinished() {
        return !commands.isScheduled();
    }
}
