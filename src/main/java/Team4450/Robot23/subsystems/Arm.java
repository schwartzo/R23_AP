package Team4450.Robot23.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static Team4450.Robot23.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import Team4450.Lib.Util;

public class Arm extends CommandBase {
    private CANSparkMax extenderMotor = new CANSparkMax(ARM_MOTOR, MotorType.kBrushless);
    private RelativeEncoder extenderEncoder = extenderMotor.getEncoder();
    private DigitalInput extensionLimiter = new DigitalInput(ARM_SWITCH);

    private final double MAX_ARM_ROTATIONS = 1000;

    public Arm() {
        Util.consoleLog();
    }

    public void setPower(double power) {
        if (extensionLimiter.get())
            extenderEncoder.setPosition(0);

        extenderMotor.set(
            ((power < 0 && CURRENT_ARM_ROTATIONS() >= MAX_ARM_ROTATIONS)
            || (power > 0 && fullyRetracted()))
                // If the arm is trying to extend (which is the negative value) and the current
                // position is at or above the max position, or it's trying to retract (which is
                // the positive value) but it's at the switch that says it's fully retracted it
                // stops the motor
                ? 0
                // Otherwise the arm is within bounds and it's power is set as normal
                : power);

        updateDS();
    }

    public void stopMotor() {
        extenderMotor.stopMotor();
    }

    public double getCurrentPosition() {
        return extenderEncoder.getPosition();
    }

    public boolean fullyRetracted() {
        return extensionLimiter.get();
    }

    public void updateDS() {
        double extensionPercent = CURRENT_ARM_ROTATIONS() * 100.0f / MAX_ARM_ROTATIONS;

        SmartDashboard.putString("Extension is extened by ", extensionPercent + "%");
        SmartDashboard.putNumber("Current extension power is ", extenderMotor.get());
    }

    private double CURRENT_ARM_ROTATIONS() {
        return extenderEncoder.getPosition();
    }
}