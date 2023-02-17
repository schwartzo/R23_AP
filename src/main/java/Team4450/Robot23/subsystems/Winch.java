package Team4450.Robot23.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static Team4450.Robot23.Constants.*;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import Team4450.Lib.Util;

public class Winch extends SubsystemBase {
    private CANSparkMax winchMotor = new CANSparkMax(WINCH_MOTOR, MotorType.kBrushless);
    private RelativeEncoder winchEncoder = winchMotor.getEncoder();
    private DigitalInput winchLimiter = new DigitalInput(WINCH_SWITCH);

    private final double MAX_WINCH_ROTATIONS = 1000;

    public Winch() {
        Util.consoleLog();
    }

    public void setPower(double power) {
        if (winchLimiter.get())
            winchEncoder.setPosition(0);

        winchMotor.set(
            ((power < 0 && maxDownwardRotation())
            || (power > 0 && CURRENT_WINCH_ROTATIONS() >= MAX_WINCH_ROTATIONS))
                // If the winch is trying to rotate downward (which is the negative value) and
                // the switch says its at the downward rotation limit, or it's trying to rotate
                // upward (which is the positive value) but it's current position is at or above
                // the max position it stops the motor
                ? 0
                // Otherwise the arm is within bounds and it's power is set as normal
                : power);

        updateDS();
    }

    public void stopMotor() {
        winchMotor.stopMotor();
    }

    public double CURRENT_WINCH_ROTATIONS() {
        return winchEncoder.getPosition();
    }

    public double getMaxPosition() {
        return MAX_WINCH_ROTATIONS;
    }

    public boolean maxDownwardRotation() {
        return winchLimiter.get();
    }

    public void updateDS() {
        double winchPercent = CURRENT_WINCH_ROTATIONS() * 100.0f / MAX_WINCH_ROTATIONS;

        SmartDashboard.putString("Winch rotation is at ", winchPercent + "%");
        SmartDashboard.putNumber("Current winch power is ", winchMotor.get());
    }
}