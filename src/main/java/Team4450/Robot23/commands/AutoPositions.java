package Team4450.Robot23.commands;

import java.util.HashMap;

import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;

public class AutoPositions extends CommandBase {
    private boolean doingComboState;
    private Arm arm;
    private Winch winch;
    private Claw claw;

    private ParallelCommandGroup commands = null;
    private Command armCommand = null;
    private Command winchCommand;
    private Command clawCommand;

    AutoPositions(Arm arm, Winch winch, Claw claw, ComboStateNames comboState) {
        doingComboState = true;

        this.arm = arm;
        this.winch = winch;
        this.claw = claw;

        this.comboState = comboState;
    }

    AutoPositions(Arm arm, Winch winch, Claw claw, 
                ArmStateNames armState, WinchStateNames winchState, ClawStateNames clawState) {
        doingComboState = false;

        this.arm = arm;
        this.winch = winch;
        this.claw = claw;

        this.armState = armState;
        this.winchState = winchState;
        this.clawState = clawState;
    }

    // enum's for all the names of the different pre-set states.
    public enum ArmStateNames {
        FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING, FULLY_EXTENDED
    }

    public enum WinchStateNames {
        FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING, MAX_HEIGHT
    }

    public enum ComboStateNames {
        FULLY_CONTAINED, OBJECT_PICKUP, LOWEST_SCORING, MIDDLE_SCORING, HIGHEST_SCORING
    }

    public enum ClawStateNames {
        FULLY_OPEN, HOLDING_CUBE, HOLDING_CONE, CLOSED
    }

    // Chosen state and debugging info
    private ComboStateNames comboState;
    private ArmStateNames armState;
    private WinchStateNames winchState;
    private ClawStateNames clawState;

    // HashMap's containing the revolution amounts for certain states for the Arm,
    // Winch, and Claw
    private HashMap<ArmStateNames, Double> armStates = new HashMap<ArmStateNames, Double>();
    private HashMap<WinchStateNames, Double> winchStates = new HashMap<WinchStateNames, Double>();
    private HashMap<ClawStateNames, Double> clawStates = new HashMap<ClawStateNames, Double>();

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
        clawStates.put(ClawStateNames.FULLY_OPEN, 0.0);
        clawStates.put(ClawStateNames.HOLDING_CUBE, 0.0);
        clawStates.put(ClawStateNames.HOLDING_CONE, 0.0);
        clawStates.put(ClawStateNames.CLOSED, 0.0);

        commands = new ParallelCommandGroup();

        // Sets the commands
        armCommand = new AutoArm(arm, (doingComboState) ? armStates.get(comboState) : armStates.get(armState), 0);
        winchCommand = new AutoWinch(winch, (doingComboState) ? winchStates.get(comboState) : winchStates.get(winchState), 0);
        if (comboState == ComboStateNames.OBJECT_PICKUP)
            clawCommand = new AutoClaw(claw, clawStates.get(ClawStateNames.FULLY_OPEN), 0);

        // Adds the commands
        commands.addCommands(armCommand, winchCommand, clawCommand);

        commands.schedule();

        updateDS();
    }

    private void updateDS() {
        SmartDashboard.putString("Most recent arm/winch combo state is ", comboState.toString());
        SmartDashboard.putString("Most recent arm state is ", armState.toString());
        SmartDashboard.putString("Most recent winch state is ", winchState.toString());
        SmartDashboard.putString("Most recent claw state is ", clawState.toString());
    }
}