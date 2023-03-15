package Team4450.Robot23.commands.autonomous;

import static Team4450.Robot23.Constants.*;

import Team4450.Lib.LCD;
import Team4450.Lib.Util;
import Team4450.Robot23.Constants;
import Team4450.Robot23.RobotContainer;
import Team4450.Robot23.subsystems.DriveBase;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.RobotBase;

/**
 * A command that will drive the robot in any direction to the specified distances while
 * rotating using a motion profiled PID command and steering correction. Velocity &
 * acceleration are a guess, need to characterize the robot for good numbers. Motion
 * profile will accelerate the robot to an appropriate speed and decelerate to a stop at
 * the target distance.
 */
public class AutoCombinedDriveRotateProfiled extends MultiProfiledPIDCommand
{
    private DriveBase driveBase;

    private AutoCombinedDriveRotateProfiled instance;

    private static double kPDrive = 1.2, kIDrive = .15, kDDrive = 0, kToleranceMeters = .10;
    // Note: kP was 15 but sim does not work right as 15 causes to much rotation. 5 works in sim.
    // But, 5 may be too little for a real robot and not output enough power to move. Need to test.
    private static double kPRotate = 1.7, kIRotate = kPRotate / 10, kDRotate = .02, kToleranceRad = .01;
    private double        distanceX, distanceY, distance, targetAngle, startTime;
    private int           iterations;
    private StopMotors    stop;
    private Brakes        brakes;
    private FieldOriented fieldOriented;

    private double        currentThrottle;
    private double        currentStrafe;
    private double        currentRotation;

    private Pose2d        startPose;

    /**
     * Drives robot to the specified distance using a motion profile while
     * steering toward a given target angle.
     *
     * @param drive         The drive subsystem to use.
     * @param distanceX     The distance to drive in meters along the X axis. + is forward.
     * @param distanceY     The distance to drive in meters along the Y axis. + is left.
     * @param targetAngle   The angle to rotate in degrees. + is left.
     * @param stop          Set to stop or not stop motors at end.
     * @param brakes        If stopping, set brakes on or off.
     * @param fieldOriented Set to move relative to field or the robot.
     */
    private AutoCombinedDriveRotateProfiled(DriveBase driveBase, double distanceX, double distanceY, double targetAngle, StopMotors stop,
            Brakes brakes, FieldOriented fieldOriented)
    {
        super();
        addPID(
                "Throttle/Strafe",
                new ProfiledPIDController(kPDrive, kIDrive, kDDrive,
                        new TrapezoidProfile.Constraints(MAX_WHEEL_SPEED, MAX_WHEEL_ACCEL)),
                // Closed loop on distance via reference so pid controller can call it on each
                // execute() call.
                driveBase::getDistanceTraveled,
                // Set target distance.
                stateSupplier(() -> Math.hypot(distanceX, distanceY)),
                // Pipe output to drive robot.
                (output, setpoint) -> instance.setDirection(output * getOutputMultiplierX(distanceX, distanceY),
                        output * getOutputMultiplierY(distanceX, distanceY)));
        addPID(
                "Rotation",
                new ProfiledPIDController(kPRotate, kIDrive, kDRotate,
                        new TrapezoidProfile.Constraints(
                        Math.toRadians(Constants.MAX_ROTATIONAL_VEL),
                        Math.toRadians(Constants.MAX_ROTATIONAL_ACCEL))),
                driveBase::getYawR,
                stateSupplier(() -> Math.toRadians(targetAngle)),
                (output, setpoint) -> instance.setRotation(output));

        Util.consoleLog("distanceX=%.3fm  distanceY=%.3fm  distance=%.3fm  angle=%.3fr  rotate=%.3fr  stop=%s  brakes=%s  fieldOriented=%s",
                distanceX, distanceY, distance, Math.atan(distanceY / distanceX),
                targetAngle, stop, brakes, fieldOriented);

        Util.consoleLog("[drive]     kP=%.6f  kI=%.6f", kPDrive, kIDrive);
        Util.consoleLog("[rotation]  kP=%.6f  kI=%.6f", kPRotate, kIRotate);

        this.driveBase = driveBase;
        this.startPose = driveBase.getRobotPose();
        this.brakes = brakes;
        this.stop = stop;
        this.fieldOriented = fieldOriented;
        this.distanceX = distanceX;
        this.distanceY = distanceY;
        this.distance = Math.hypot(distanceX, distanceY);
        this.targetAngle = targetAngle;

        this.instance = this;

        getPID("Throttle/Strafe").controller.setTolerance(kToleranceMeters);
        getPID("Rotation").controller.setTolerance(kToleranceRad);
    }

    /**
     * Drives robot to the specified distance using a motion profile while
     * steering toward a given target angle.
     *
     * @param drive         The drive subsystem to use.
     * @param distanceX     The distance to drive in meters along the X axis. + is forward.
     * @param distanceY     The distance to drive in meters along the Y axis. + is left.
     * @param targetAngle   The angle to rotate in degrees. + is left.
     * @param stop          Set to stop or not stop motors at end.
     * @param brakes        If stopping, set brakes on or off.
     * @param fieldOriented Set to move relative to field or the robot.
     * @return              A new command instance with specified parameters.
     */
    public static AutoCombinedDriveRotateProfiled cartes(DriveBase driveBase, double distanceX, double distanceY,
            double targetAngle, StopMotors stop, Brakes brakes, FieldOriented fieldOriented)
    {
        return new AutoCombinedDriveRotateProfiled(driveBase, distanceX,
                distanceY, targetAngle, stop, brakes, fieldOriented);
    }

    /**
     * Drives robot to the specified distance using a motion profile while
     * steering toward a given target angle.
     *
     * @param drive         The drive subsystem to use.
     * @param distance      The distance to drive in meters. + is forward.
     * @param heading       The angle to drive at in degrees. + is left.
     * @param stop          Set to stop or not stop motors at end.
     * @param brakes        If stopping, set brakes on or off.
     * @param fieldOriented Set to move relative to field or the robot.
     * @return              A new command instance with specified parameters.
     */
    public static AutoCombinedDriveRotateProfiled polar(DriveBase driveBase, double distance, double heading,
            double targetAngle, StopMotors stop, Brakes brakes, FieldOriented fieldOriented)
    {
        return new AutoCombinedDriveRotateProfiled(driveBase, distance * Math.cos(Math.toRadians(heading)),
                distance * Math.sin(Math.toRadians(heading)), targetAngle, stop, brakes, fieldOriented);
    }

    @Override
    public void initialize()
    {
        Util.consoleLog("distanceX=%.3fm  distanceY=%.3fm  distance=%.3fm  stop=%s  brakes=%s  fieldOriented=%s",
                distanceX, distanceY, distance, stop, brakes, fieldOriented);

        startTime = Util.timeStamp();

        if (brakes == Brakes.on)
            driveBase.setBrakeMode(true);
        else
            driveBase.setBrakeMode(false);

        driveBase.resetDistanceTraveled();

        if (fieldOriented == FieldOriented.on && !driveBase.getFieldOriented())
            driveBase.toggleFieldOriented();

        driveBase.resetYaw();
    }

    public void execute()
    {
        Util.consoleLog();

        double yaw = -driveBase.getYaw(); // Invert to swerve angle convention.

        super.execute();

        LCD.printLine(LCD_5, "Wheel distance=%.3f", driveBase.getDistanceTraveled());

        driveBase.drive(currentThrottle, currentStrafe, currentRotation);

        Util.consoleLog("[drive]     tg=%.3f  dist=%.3f  sp=%.3f  err=%.3f",
                getPID("Throttle/Strafe").controller.getGoal().position, driveBase.getDistanceTraveled(),
                getPID("Throttle/Strafe").controller.getSetpoint().position,
                getPID("Throttle/Strafe").controller.getPositionError());

        Util.consoleLog("[rotation]  tg=%.3fr  sp=%.3fr  m=%.3fr  err=%.3f", getPID("Rotation").controller.getGoal().position,
                getPID("Rotation").controller.getSetpoint().position, getPID("Rotation").measurement.get(),
                getPID("Rotation").controller.getPositionError());       

        Util.consoleLog("yaw=%.2f  hdng=%.2f ", yaw, RobotContainer.navx.getHeading());

        iterations++;

        System.out.println("Traveled distance at iteration " + iterations + " is " + driveBase.getDistanceTraveled() + ", want to reach " + distance);
    }

    @Override
    public boolean isFinished()
    {
        // End when the target distance is reached. The PIDController atGoal and
        // atSetPoint
        // functions do not seem to work. Don't know why.

        return Math.abs(Math.abs(distance) - Math.abs(driveBase.getDistanceTraveled())) <= kToleranceMeters &&
                Math.abs((getPID("Rotation").controller.getGoal().position - getPID("Rotation").measurement.get())) <= kToleranceRad;
    }

    @Override
    public void end(boolean interrupted)
    {
        Util.consoleLog("interrupted=%b", interrupted);

        if (stop == StopMotors.stop)
            driveBase.stop();

        double actualDist = Math.abs(driveBase.getDistanceTraveled());

        Util.consoleLog("end: [drive]     target=%.3f  actual=%.3f  error=%.2f pct",
                Math.abs(distance), actualDist, (actualDist - Math.abs(distance)) / Math.abs(distance) * 100.0);

        Util.consoleLog("end: [rotation]  yaw=%.2f  hdng=%.2f  target=%.2f  error=%.2f pct", driveBase.getYaw(), 
                RobotContainer.navx.getHeading(), targetAngle,
                (targetAngle - driveBase.getYaw()) / Math.abs(targetAngle) * 100.0);

        if (fieldOriented == FieldOriented.on && driveBase.getFieldOriented())
            driveBase.toggleFieldOriented();

        Util.consoleLog("iterations=%d  elapsed time=%.3fs", iterations, Util.getElaspedTime(startTime));

        Util.consoleLog("end ---------------------------------------------------------------");
    }

    private void setDirection(double throttle, double strafe)
    {
        currentThrottle = throttle;
        currentStrafe = strafe;
    }

    private void setRotation(double rotation)
    {
        currentRotation = rotation * (RobotBase.isSimulation() ? -1 : 1);
    }

    private double getDisplacement()
    {
        return Math.hypot(startPose.getX() - driveBase.getRobotPose().getX(),
                startPose.getY() - driveBase.getRobotPose().getY());
    }

    private static double getOutputMultiplierX(double distanceX, double distanceY)
    {
        return Math.abs(distanceX) > Math.abs(distanceY) ?
            sign(distanceX) :
            (Math.abs(distanceX) / Math.abs(distanceY)) * sign(distanceX);
    }

    private static double getOutputMultiplierY(double distanceX, double distanceY)
    {
        return Math.abs(distanceY) > Math.abs(distanceX) ?
            sign(distanceY) :
            (Math.abs(distanceY) / Math.abs(distanceX)) * sign(distanceY);
    }

    private static double sign(double x)
    {
        return x == 0 ? 0 : x / Math.abs(x);
    }

    public enum Brakes
    {
        off,
        on
    }

    public enum StopMotors {
        dontStop,
        stop
    }

    public enum FieldOriented {
        off,
        on
    }
}
