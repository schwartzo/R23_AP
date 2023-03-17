package Team4450.Robot23.commands.autonomous;

import Team4450.Lib.LCD;
import Team4450.Lib.Util;
import Team4450.Robot23.RobotContainer;
import Team4450.Robot23.commands.CommandSimplifier;
import Team4450.Robot23.commands.AutoPositions.ArmStateNames;
import Team4450.Robot23.commands.AutoPositions.ClawStateNames;
import Team4450.Robot23.commands.AutoPositions.ComboStateNames;
import Team4450.Robot23.commands.CommandSimplifier.CommandType;
import Team4450.Robot23.commands.CommandSimplifier.CommandType.commandType;
import Team4450.Robot23.pathfinder.Path;
import Team4450.Robot23.pathfinder.STranslation2d;
import Team4450.Robot23.pathfinder.command.ExecutePathCommand;
import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.DriveBase;
import Team4450.Robot23.subsystems.Winch;

import static Team4450.Robot23.Constants.*;

import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

/**
 * This is an example autonomous command based on 4450 customized version of
 * Wpilib Trajetory following commands.
 */
public class TestingAuto extends CommandBase
{
	private final DriveBase driveBase;
	private final Arm arm;
	private final Winch winch;
	private final Claw claw;
	
	private SequentialCommandGroup	commands = null;
	private	Pose2d					startingPose;   

	/**
	 * Creates a new TestAuto3 autonomous command. This command demonstrates one
	 * possible structure for an autonomous command and shows the use of the 
	 * autonomous driving support commands.
	 *
	 * @param driveBase DriveBase subsystem used by this command to drive the robot.
	 * @param startingPose Starting pose of the robot.
	 */
	public TestingAuto(DriveBase driveBase, Arm arm, Winch winch, Claw claw, Pose2d startingPose) 
	{
		Util.consoleLog();
		
		this.driveBase = driveBase;
		this.arm = arm;
		this.winch = winch;
		this.claw = claw;

		this.startingPose = startingPose;

		// Use addRequirements() here to declare subsystem dependencies.
		addRequirements(this.driveBase, this.arm, this.winch, this.claw);
	}
	
	/**
	 * Called when the command is initially scheduled. (non-Javadoc)
	 * @see edu.wpi.first.wpilibj2.command.Command#initialize()
	 */
	@Override
	public void initialize() 
	{
		Util.consoleLog();

		SmartDashboard.putBoolean("Autonomous Active", true);
		
	  	LCD.printLine(LCD_1, "Mode: Auto - TestingAuto - All=%s, Location=%d, FMS=%b, msg=%s", alliance.name(), location, 
				DriverStation.isFMSAttached(), gameMessage);
		
		// Reset drive base distance tracking.	  	
	  	driveBase.resetDistanceTraveled();
	  	
	  	// Set drive base yaw tracking to 0.
	  	driveBase.resetYaw();

		// Set heading to initial angle (0 is robot pointed down the field) so
		// NavX class can track which way the robot is pointed all during the match.
		RobotContainer.navx.setHeading(startingPose.getRotation().getDegrees());
			
		// Target heading should be the same.
		RobotContainer.navx.setTargetHeading(startingPose.getRotation().getDegrees());
		
		// Since a typical autonomous program consists of multiple actions, which are commands
		// in this style of programming, we will create a list of commands for the actions to
		// be taken in this auto program and add them to a sequential command list to be 
		// executed one after the other until done.
		
		commands = new SequentialCommandGroup();

		Path<STranslation2d> path = new Path.Builder<STranslation2d>(driveBase.getRobotPose().getTranslation(), new STranslation2d(new Translation2d(10, new Rotation2d())))
				.command((tr) -> AutoDriveDiagonalProfiled.cartes(driveBase, tr.base().getX(), tr.base().getY(), AutoDriveDiagonalProfiled.StopMotors.stop, AutoDriveDiagonalProfiled.Brakes.on, AutoDriveDiagonalProfiled.FieldOriented.on))
				.build();

		CommandSimplifier.simplify(commands, driveBase, arm, winch, claw, new CommandType[] {
			new CommandType(new Translation2d(1, 2)),
			new CommandType(new Translation2d(-1, -3)),
			new CommandType(new Translation2d(0, 1)),
			new CommandType(ComboStateNames.OBJECT_PICKUP),
			new CommandType(null, null, ClawStateNames.HOLDING_CUBE)
		});
		
		commands.addCommands(new ExecutePathCommand(path));

		commands.schedule();
	}
	
	/**
	 *  Called every time the scheduler runs while the command is scheduled.
	 *  In this model, this command just idles while the Command Group we
	 *  created runs on its own executing the steps (commands) of this Auto
	 *  program.
	 */
	@Override
	public void execute() 
	{
	}
	
	/**
	 *  Called when the command ends or is interrupted.
	 */
	@Override
	public void end(boolean interrupted) 
	{
		Util.consoleLog("interrupted=%b", interrupted);
		
		driveBase.stop();
		
		Util.consoleLog("final heading=%.2f  Radians=%.2f", RobotContainer.navx.getHeading(), RobotContainer.navx.getHeadingR());
		Util.consoleLog("end -----------------------------------------------------");

		SmartDashboard.putBoolean("Autonomous Active", false);
	}
	
	/**
	 *  Returns true when this command should end. That should be when
	 *  all the commands in the command list have finished.
	 */
	@Override
	public boolean isFinished() 
	{
		// Note: commands.isFinished() will not work to detect the end of the command list
		// due to how FIRST coded the SquentialCommandGroup class. 
		
		return !commands.isScheduled();
    }

}
