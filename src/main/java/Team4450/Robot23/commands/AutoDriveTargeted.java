package Team4450.Robot23.commands;

import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.FieldOriented;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.StopMotors;
import Team4450.Robot23.subsystems.DriveBase;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class AutoDriveTargeted extends CommandBase {
    private DriveBase driveBase;

    private Translation2d targetCoords;

    private SequentialCommandGroup commandGroup;

    public AutoDriveTargeted(DriveBase driveBase, Translation2d targetCoords) {
        this.driveBase = driveBase;
        this.targetCoords = new Translation2d(targetCoords.getX() - driveBase.getOdometry().getEstimatedPosition().getX(), 
                                              targetCoords.getY() - driveBase.getOdometry().getEstimatedPosition().getY());
    }

    public AutoDriveTargeted(DriveBase driveBase, double xPosition, double yPosition) {
        this.driveBase = driveBase;
        this.targetCoords = new Translation2d(xPosition - driveBase.getOdometry().getEstimatedPosition().getX(), 
                                              yPosition - driveBase.getOdometry().getEstimatedPosition().getY());
    }

    @Override
    public void initialize() {
        commandGroup = new SequentialCommandGroup();

        commandGroup.addCommands(AutoDriveDiagonalProfiled.cartes(driveBase, targetCoords.getX(), targetCoords.getY(), StopMotors.stop, Brakes.on, FieldOriented.on));

        commandGroup.schedule();
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return commandGroup.isFinished();
    }

    @Override
    public void end(boolean interrupted) {

    }
}
