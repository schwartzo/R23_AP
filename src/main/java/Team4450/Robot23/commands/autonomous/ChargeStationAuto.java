package Team4450.Robot23.commands.autonomous;

import static Team4450.Robot23.Constants.*;

import Team4450.Robot23.commands.AutoBalance;
import Team4450.Robot23.commands.AutoPositions;
import Team4450.Robot23.commands.CommandSimplifier;
import Team4450.Robot23.commands.AutoPositions.ClawStateNames;
import Team4450.Robot23.commands.AutoPositions.ComboStateNames;
import Team4450.Robot23.commands.CommandSimplifier.CommandType;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.StopMotors;
import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.DriveBase;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ChargeStationAuto extends CommandBase {
    private final DriveBase driveBase;
    private final Arm arm;
    private final Winch winch;
    private final Claw claw;
    
    private SequentialCommandGroup commands = null;
    private Command command = null;

    public ChargeStationAuto(DriveBase driveBase, Arm arm, Winch winch, Claw claw) {
        this.driveBase = driveBase;
        this.arm = arm;
        this.winch = winch;
        this.claw = claw;

        addRequirements(this.driveBase);
        addRequirements(this.arm);
        addRequirements(this.winch);
        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        commands = new SequentialCommandGroup();

        // // Rotate robot torwards target scoring position. Done before letting arm and winch move so it doesn't accidently run into anything.
        // command = new AutoRotate(driveBase, 180);
        // commands.addCommands(command);

        // // Move set arm and winch up for target scoring position
        // command = new AutoPositions(arm, winch, claw, ComboStateNames.HIGHEST_SCORING);
        // commands.addCommands(command);

        // // Drop the cone
        // command = new AutoPositions(arm, winch, claw, null, null, ClawStateNames.FULLY_OPEN);
        // commands.addCommands(command);

        // // Exit the community zone
        // command = new AutoDriveProfiled(driveBase, 3.7, StopMotors.stop, Brakes.on);
        // commands.addCommands(command);

        // // Move in front of the charging station
        // command = new AutoStrafeProfiled(driveBase, 2.4, StopMotors.stop, Brakes.on);
        // commands.addCommands(command);

        // // Get on the charging station
        // command = new AutoBalance(driveBase, -0.5);
        // commands.addCommands(command);

        commands.addCommands(new CommandSimplifier(driveBase, arm, winch, claw, new CommandType[] {
            new CommandType(180),
            new CommandType(ComboStateNames.HIGHEST_SCORING),
            new CommandType(null, null, ClawStateNames.FULLY_OPEN),
            new CommandType(new Translation2d(3.7, 0)),
            new CommandType(new Translation2d(0, 2.4)),
        }), new AutoBalance(driveBase, -0.5));

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
