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

    public AutoPositions(Arm arm, Winch winch, Claw claw, ComboStateNames comboState) {
        doingComboState = true;

        this.arm = arm;
        this.winch = winch;
        this.claw = claw;

        this.comboState = comboState;
        this.armStates.get(comboStates.get(comboState).armState);
        this.winchStates.get(comboStates.get(comboState).winchState); 
        if (comboState == ComboStateNames.OBJECT_PICKUP) this.clawState = ClawStateNames.FULLY_OPEN;

        addRequirements(this.arm);
        addRequirements(this.winch);
        addRequirements(this.claw);
    }

    public AutoPositions(Arm arm, Winch winch, Claw claw,
            ArmStateNames armState, WinchStateNames winchState, ClawStateNames clawState) {
        doingComboState = false;

        this.arm = arm;
        this.winch = winch;
        this.claw = claw;

        this.armState = armState;
        this.winchState = winchState;
        this.clawState = clawState;

        addRequirements(this.arm);
        addRequirements(this.winch);
        addRequirements(this.claw);
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

    private static class ComboState {
        public final ArmStateNames armState;
	    public final WinchStateNames winchState;

        public ComboState(ArmStateNames armState, WinchStateNames winchState) {
            this.armState = armState;
            this.winchState = winchState;
        }
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
    private HashMap<ComboStateNames, ComboState> comboStates = new HashMap<ComboStateNames, ComboState>();

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

        // comboStates
	    comboStates.put(ComboStateNames.FULLY_CONTAINED, new ComboState(ArmStateNames.FULLY_CONTAINED, WinchStateNames.FULLY_CONTAINED));
        comboStates.put(ComboStateNames.OBJECT_PICKUP, new ComboState(ArmStateNames.OBJECT_PICKUP, WinchStateNames.OBJECT_PICKUP));
        comboStates.put(ComboStateNames.LOWEST_SCORING, new ComboState(ArmStateNames.LOWEST_SCORING, WinchStateNames.LOWEST_SCORING));
        comboStates.put(ComboStateNames.MIDDLE_SCORING, new ComboState(ArmStateNames.MIDDLE_SCORING, WinchStateNames.MIDDLE_SCORING));
        comboStates.put(ComboStateNames.HIGHEST_SCORING, new ComboState(ArmStateNames.HIGHEST_SCORING, WinchStateNames.HIGHEST_SCORING));

        commands = new ParallelCommandGroup();

        // Sets and adds the commands
        if (armState != null || (doingComboState && comboState != null)) {
            armCommand = new AutoArm(arm, armStates.get(armState), 0);
	        commands.addCommands(armCommand);
	    }

        if (winchState != null || (doingComboState && comboState != null)) {
            winchCommand = new AutoWinch(winch, winchStates.get(winchState), 0);
	        commands.addCommands(winchCommand);
	    }
        
        if (clawState != null || (doingComboState && comboState == ComboStateNames.OBJECT_PICKUP)) {
            clawCommand = new AutoClaw(claw, clawStates.get(clawState), 0);
            commands.addCommands(clawCommand);
	    }

        commands.schedule();

        updateDS();
    }

    private void updateDS() {
        if (doingComboState && comboState != null)
            SmartDashboard.putString("Most recent arm/winch combo state is ", comboState.toString());
        if ((doingComboState && comboState != null) || armState != null)
            SmartDashboard.putString("Most recent arm state is ", armState.toString());
        if ((doingComboState && comboState != null) || winchState != null)
            SmartDashboard.putString("Most recent winch state is ", winchState.toString());
        if ((doingComboState && comboState != null) || clawState != null)
            SmartDashboard.putString("Most recent claw state is ", clawState.toString());
    }
}
