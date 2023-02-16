package Team4450.Robot23.commands;

import java.util.HashMap;

import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class AutoPositions extends CommandBase {
    private boolean doingComboState = false;
    private Arm arm;
    private Winch winch;
    private Claw claw;

    AutoPositions(Arm arm, Winch winch, Claw claw) {
        this.arm = arm;
        this.winch = winch;
        this.claw = claw;
    }


    // enum's for all the names of the different pre-set states.
    public enum ArmStateNames {
        FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING, FULLY_EXTENDED
    }

    public enum WinchStateNames {
        FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING, MAX_HEIGHT
    }

    public enum ComboStateNames {
        NONE, FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING
    }

    public enum ClawStates {
        FULLY_OPEN, HOLDING_CUBE, HOLDING_CONE, CLOSED
    }

    // Debugging info
    private ArmStateNames lastArmState;
    private WinchStateNames lastWinchState;
    private ComboStateNames lastComboState;
    private ClawStates lastClawState;

    // HashMap's containing the revolution amounts for certain states for the Arm,
    // Winch, and Claw
    private HashMap<ArmStateNames, Double> armStates = new HashMap<ArmStateNames, Double>();
    private HashMap<WinchStateNames, Double> winchStates = new HashMap<WinchStateNames, Double>();
    private HashMap<ClawStates, Double> clawStates = new HashMap<ClawStates, Double>();

    // The rest of the constructor variables


    public void initialize() {
        // Creating inital states, where the doubles are target revolutions
        // armStates
        armStates.put(ArmStateNames.FULLY_CONTAINED, 0.0); // ALL DOUBLES ARE PLACEHOLDER VALUES
        armStates.put(ArmStateNames.OBJECT_PICKUP, 0.0);
        armStates.put(ArmStateNames.LOWEST_SCORING, 0.0);
        armStates.put(ArmStateNames.MIDDLE_SCORING, 0.0);
        armStates.put(ArmStateNames.HIGHEST_SCORING, 0.0);
        armStates.put(ArmStateNames.FULLY_EXTENDED, 0.0);

        // winchStates
        winchStates.put(WinchStateNames.FULLY_CONTAINED, 0.0);
        winchStates.put(WinchStateNames.OBJECT_PICKUP, 0.0);
        winchStates.put(WinchStateNames.LOWEST_SCORING, 0.0);
        winchStates.put(WinchStateNames.MIDDLE_SCORING, 0.0);
        winchStates.put(WinchStateNames.HIGHEST_SCORING, 0.0);
        winchStates.put(WinchStateNames.MAX_HEIGHT, 0.0);

        // clawStates
        clawStates.put(ClawStates.FULLY_OPEN, 0.0);
        clawStates.put(ClawStates.HOLDING_CUBE, 0.0);
        clawStates.put(ClawStates.HOLDING_CONE, 0.0);
        clawStates.put(ClawStates.CLOSED, 0.0);

        // Setting initial states
        
        updateDS();
    }

    private void updateDS() {
        SmartDashboard.putString("Most recent arm/winch combo state is ", lastComboState.toString());
        SmartDashboard.putString("Most recent arm state is ", lastArmState.toString());
        SmartDashboard.putString("Most recent winch state is ", lastWinchState.toString());
        SmartDashboard.putString("Most recent claw state is ", lastClawState.toString());
    }
}