package Team4450.Robot23.commands;

import Team4450.Robot23.commands.autonomous.AutoCombinedDriveRotateProfiled;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled;
import Team4450.Robot23.commands.autonomous.AutoRotate;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.FieldOriented;
import Team4450.Robot23.commands.autonomous.AutoDriveDiagonalProfiled.StopMotors;
import Team4450.Robot23.subsystems.Arm;
import Team4450.Robot23.subsystems.Claw;
import Team4450.Robot23.subsystems.DriveBase;
import Team4450.Robot23.subsystems.Winch;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CommandSimplifier {

    public static void simplify(SequentialCommandGroup commands, DriveBase driveBase, Arm arm, Winch winch, Claw claw, CommandType... commandTypes) {

        for (CommandType command : commandTypes) {
            switch(command.type) {
                case AutoDriveDiagonalProfiled:
                    commands.addCommands(AutoDriveDiagonalProfiled.cartes(driveBase, 
                                                                                command.coordinates.getX(), 
                                                                                command.coordinates.getY(), 
                                                                                StopMotors.stop, Brakes.on, 
                                                                                FieldOriented.on));
                    break;

                case Rotate:
                    commands.addCommands(new AutoRotate(driveBase, command.rotation));
                    break;

                case AutoCombinedDriveRotateProfiled:
                    commands.addCommands(AutoCombinedDriveRotateProfiled.cartes(driveBase, 
                                                                                    command.coordinates.getX(), 
                                                                                    command.coordinates.getY(), 
                                                                                    command.rotation, 
                                                                                    AutoCombinedDriveRotateProfiled.StopMotors.stop, 
                                                                                    AutoCombinedDriveRotateProfiled.Brakes.on, 
                                                                                    AutoCombinedDriveRotateProfiled.FieldOriented.on));

                case AutoPositionsCombo:
                    commands.addCommands(new AutoPositions(arm, winch, claw, command.comboState));
                    break;

                case AutoPositionsManual:
                    commands.addCommands(new AutoPositions(arm, winch, claw, command.armState, command.winchState, command.clawState));
                    break;
            }
        }
    }

    public static class CommandType {
        public enum commandType {
            AutoDriveDiagonalProfiled,
            Rotate,
            AutoCombinedDriveRotateProfiled,
            AutoPositionsCombo,
            AutoPositionsManual
        }

        commandType type;

        Translation2d coordinates;
        public CommandType(Translation2d coordinates) {
            this.coordinates = coordinates;
            this.type = commandType.AutoDriveDiagonalProfiled;
        }

        public static CommandType moveRobot(double x, double y) {
            return new CommandType(new Translation2d(x, y));
        }


        double rotation;
        public CommandType(double rotation) {
            this.rotation = rotation;
            this.type = commandType.Rotate;
        }

        public static CommandType rotateRobot(double rotation) {
            return new CommandType(rotation);
        }


        public CommandType(double x, double y, double rotation) {
            this.coordinates = new Translation2d(x, y);
            this.rotation = rotation;
            this.type = commandType.AutoCombinedDriveRotateProfiled;
        }

        public static CommandType moveAndRotate(double x, double y, double rotation) {
            return new CommandType(x, y, rotation);
        }


        AutoPositions.ComboStateNames comboState;
        public CommandType(AutoPositions.ComboStateNames comboState) {
            this.comboState = comboState;
            this.type = commandType.AutoPositionsCombo;
        }

        public static CommandType comboPosition(AutoPositions.ComboStateNames comboState) {
            return new CommandType(comboState);
        }


        AutoPositions.ArmStateNames armState;
        AutoPositions.WinchStateNames winchState;
        AutoPositions.ClawStateNames clawState;
        public CommandType(AutoPositions.ArmStateNames armState, 
                           AutoPositions.WinchStateNames winchState, 
                           AutoPositions.ClawStateNames clawState) {
            this.armState = armState;
            this.winchState = winchState;
            this.clawState = clawState;
            this.type = commandType.AutoPositionsManual;
        }

        public static CommandType manualPosition(AutoPositions.ArmStateNames armState, 
                                                 AutoPositions.WinchStateNames winchState, 
                                                 AutoPositions.ClawStateNames clawState) {
            return new CommandType(armState, winchState, clawState);
        }
    }        
}
/*
 * traj
    * start
    * target 1
    * target 2
    * target 3
    * target 4
 * 
 * commands
    * cmnd 1 at 2
    * cmnd 2 at 4
 * 
 * goal
    * target 1
    * cmnd 1
    * target 2
    * target 3
    * cmnd 2
    * target 4 as final
 * 
 * take away
    * command is before target at the same index
    * 
    * inital = previous target - 1
    * first target = previous target
    * final target = new target - 1
    * final = new target
 */

 /*
  * DriveBase driveBase;

    Translation2d[] movementArray;
    commandWithIndex[] commandsWithIndex;

    List<Command> fullCommandList = new ArrayList<Command>();

    public static class commandWithIndex {
        Command command;
        int commandIndex;

        public commandWithIndex(Command command, int commandIndex) {
            this.command = command;
            this.commandIndex = commandIndex;
        }
    }

    SequentialCommandGroup seqCommandGroup;

    public CommandMerger(DriveBase driveBase, Translation2d[] movementArray, commandWithIndex[] commandsWithIndex) {
        this.driveBase = driveBase;
        this.movementArray = movementArray;
        this.commandsWithIndex = commandsWithIndex;
    }

    public void initialize() {
        seqCommandGroup = new SequentialCommandGroup();

        for (int moveIndex = 0; moveIndex < movementArray.length; moveIndex++) {
            for (int commandIndex = moveIndex; commandIndex < commandsWithIndex.length; commandIndex++) {
                if (moveIndex <= commandsWithIndex[commandIndex].commandIndex) {
                    fullCommandList.add(commandsWithIndex[commandIndex].command);
                }
            }

            // A command that allows me to move on both x and y axis at same time torwards a target destination.
            // fullCommandList.add(new AutoSwerveDriveProfiled(driveBase, movementArray[moveIndex].getX(), movementArray[moveIndex].getY(), StopMotors.stop, Brakes.on));
        }

        System.out.println("Amount of commands is " + fullCommandList.size());

        seqCommandGroup.schedule();

        // // trajectoryList needs to be more than 1 because I cut out the initial pose
        // if (trajectoryList.size() < 2 || commandsWithIndex.length == 0) {
        //     // The point of this currently is to merge commands into a trajectory. If either
        //     // has nothing inside of it, it is useless.
        //     return;
        // }

        // for (int i = 1; i < trajectoryList.size() - 1; i++) {
        //     convertedTrajList.add(new commandWithIndex(new AutoDriveProfiled(driveBase, trajectoryList.get(i).poseMeters.getX() - driveBase.getOdometry().getEstimatedPosition().getX(), StopMotors.stop, Brakes.on), i));
        //     convertedTrajList.add(new commandWithIndex(new AutoStrafeProfiled(driveBase, trajectoryList.get(i).poseMeters.getY() - driveBase.getOdometry().getEstimatedPosition().getY(), StopMotors.stop, Brakes.on), i));
        // }

        // for (int a = 0; a < commandsWithIndex.length - 1; a++) {
        //     for (int b = 0; b < convertedTrajList.size() - 1; b++) {
        //         if (convertedTrajList.get(b).commandIndex < commandsWithIndex[a].commandIndex && convertedTrajList.get(b) != null) {
        //             fullCommandList.add(convertedTrajList.get(b).command);
        //             seqCommandGroup.addCommands(convertedTrajList.get(b).command);
        //             convertedTrajList.set(b, null);
        //         }
        //     }

        //     fullCommandList.add(commandsWithIndex[a].command);
        //     seqCommandGroup.addCommands(commandsWithIndex[a].command);
        // }

        // for (int a = 0; a < convertedTrajList.size() - 1; a++) {
        //     fullCommandList.add(convertedTrajList.get(a).command);
        //     seqCommandGroup.addCommands(convertedTrajList.get(a).command);
        // }
    }
  */