package Team4450.Robot23.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static Team4450.Robot23.Constants.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import Team4450.Lib.FXEncoder;
import Team4450.Lib.Util;

public class Claw extends SubsystemBase {
    private TalonFX clawMotor = new TalonFX(CLAW_MOTOR);
    private FXEncoder clawEncoder = new FXEncoder();;
    private DigitalInput clawLimiter = new DigitalInput(CLAW_SWITCH);

    private final double MAX_CLAW_ENCODER_COUNT = 1000;

    public Claw() {
        Util.consoleLog();
    }

    public void setPower(double power) {
        if (clawLimiter.get())
            clawEncoder.reset();

        clawMotor.set(ControlMode.PercentOutput,
            ((power < 0 && CURRENT_CLAW_ENCODER_COUNT() >= MAX_CLAW_ENCODER_COUNT)
            || (power > 0 && fullyRetracted()))
                // If the arm is trying to close (which is the negative value) and the current
                // encoder count of the claw is a or above the the max encoder count prevent it
                // from closing further, and if the claw is trying to open but its at the switch
                // that says its fully open prevent it from opening further
                ? 0
                // Otherwise the arm is within bounds and it's power is set as normal
                : power);

        updateDS();
    }

    public void stopMotor() {
        clawMotor.set(ControlMode.PercentOutput, 0);
    }

    public double CURRENT_CLAW_ENCODER_COUNT() {
        return clawEncoder.get();
    }

    public double getMaxEncoder() {
        return MAX_CLAW_ENCODER_COUNT;
    }

    public boolean fullyRetracted() {
        return clawLimiter.get();
    }

    public void updateDS() {
        double openPercent = CURRENT_CLAW_ENCODER_COUNT() * 100.0f / MAX_CLAW_ENCODER_COUNT;

        SmartDashboard.putString("Claw is ", openPercent + "% opened");
        SmartDashboard.putNumber("Current extension power is ", clawMotor.getMotorOutputPercent() / 100);
    }
}