package Team4450.Robot23.commands;

// import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import Team4450.Lib.SynchronousPID;
import Team4450.Lib.Util;
import Team4450.Robot23.RobotContainer;
import Team4450.Robot23.subsystems.DriveBase;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoBalance extends CommandBase {
    private final DriveBase driveBase;

    private double pitch; // Rotation around x axis
    private double yaw; // Rotation around z axis
    private double degreeLeeway = 1;

    // A speed that shouldn't ever happen in this, so can be used for checking if we
    // are skipping the rotation part
    private double initialSpeed = 0.0;

    private SynchronousPID pidController;
    private double Kp, Ki, Kd;

    public AutoBalance(DriveBase driveBase) {
        Util.consoleLog();

        this.driveBase = driveBase;

        addRequirements(this.driveBase);
    }

    public AutoBalance(DriveBase driveBase, double speed) {
        Util.consoleLog();

        this.driveBase = driveBase;
        this.initialSpeed = speed;

        addRequirements(this.driveBase);
    }

    public void initialize() {
        pidController = new SynchronousPID(Kp, Ki, Kd);

        if (initialSpeed == 0)
            resetYaw();
        else
            balanceRobot(initialSpeed);
    }

    private void recalibrateRotation() {
        // Adding 180 because getPitch() and getYaw() go from -180 to 180,but im
        // treating it in the code as 0 to 360, and I don't want to edit it
        pitch = RobotContainer.navx.getAHRS().getPitch() + 180;
        yaw = RobotContainer.navx.getAHRS().getYaw() + 180;
    }

    // USING WHILES BECAUSE WE DONT WANT IT TO CONTINUE UNTIL THAT STAGE IS FINISHED

    public void resetYaw() {
        recalibrateRotation();
        double rotationTarget = 0;
        boolean calculateRotation = false;

        // While the robot rotation is not a multiple of 180 continue rotation
        while (Math.abs(yaw) % 180 != 0)
            if (calculateRotation) {
                // if closer to 180, move to 180, else to 0
                rotationTarget = pidController.calculate((yaw > 90 && yaw < 270) ? yaw + 180 : yaw,
                        Util.getElaspedTime());

                driveBase.drive(0, 0, rotationTarget);

                recalibrateRotation();
            }

        // Stops all the driveBase motors which prevents further rotation in case they
        // are not fully stopped
        driveBase.drive(0, 0, 0);

        // If the robot is facing the ramp move forward, otherwise that means the robot
        // is facing away from the ramp (and is rotated 180 degrees) so move backward
        balanceRobot((yaw == 0) ? 0.2 : -0.2);
    }

    public void balanceRobot(double speed) {
        // In case the robot starts entirely off the ramp. Even if it doesnt start off
        // the ramp it doesn't matter as if its already on the ramp angled past leeway
        // then it skips this, and if its on top of the ramp then it will get off, but
        // the rest will move it back on. Also, why would this be called if we already
        // flat on charging station?
        driveBase.drive(speed, 0, 0);
        while (Math.abs(pitch) <= degreeLeeway) {
            recalibrateRotation();
        }

        // Sets the robots power to move upward the ramp. I do this by making the
        // degrees act as -180 to 180, then checking if its negative or positive. If
        // it's negative the front of the robot is tilted down which means it needs to
        // move backward, if it spositive the front of the robot is tilted up which
        // means it needs to move forward.
        speed *= (pitch - 180) > 0 ? 1 : -1;
        driveBase.drive(speed, 0, 0);

        while (Math.abs(pitch) > degreeLeeway) {
            recalibrateRotation();
        }

        // All the qualifications have been completed, so it stops the robot motors.
        driveBase.drive(0, 0, 0);
    }

    // THIS IS A PRESS AND RUN ONCE COMMAND, ONCE THE PREVIOUS METHOD IS DONE
    // NOTHING HAPPENS. THIS MEANS I DO NOT MAKE A execute(), end(), AND
    // isFinished()

    @Override
    public void execute()
    {
        Util.consoleLog("executed");
    }
}