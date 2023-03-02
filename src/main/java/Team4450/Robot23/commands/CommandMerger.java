package Team4450.Robot23.commands;

import java.util.List;

import Team4450.Robot23.commands.autonomous.AutoDriveTrajectory;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.Brakes;
import Team4450.Robot23.commands.autonomous.AutoDriveProfiled.StopMotors;
import Team4450.Robot23.subsystems.DriveBase;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.Trajectory.State;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class CommandMerger extends CommandBase {
    DriveBase driveBase;

    Trajectory trajectory;
    List<State> trajectoryList;
    List<State> partialTrajList;
    commandWithIndex[] commandsWithIndex;

    public static class commandWithIndex {
        Command command;
        int commandIndex;

        public commandWithIndex(Command command, int commandIndex) {
            this.command = command;
            this.commandIndex = commandIndex;
        }
    }

    SequentialCommandGroup seqCommandGroup;

    private int trajStatesCompelted = 0;
    private int commandsCompleted = 0;
    private int previousTargetIndex;
    private int targetIndex;
    private boolean finishedSetup = false;

    public CommandMerger(DriveBase driveBase, Trajectory trajectory, commandWithIndex... commandsWithIndex) {
        this.driveBase = driveBase;
        this.trajectory = trajectory;
        this.trajectoryList = trajectory.getStates();
        this.commandsWithIndex = commandsWithIndex;
    }

    public void initialize() {
        if (trajectoryList.size() == 0 || commandsWithIndex.length == 0) {
            // The point of this currently is to merge commands into a trajectory. If either
            // has nothing inside of it, it is useless.
            return;
        }

        while (!finishedSetup) {
            previousTargetIndex = targetIndex;
            targetIndex = commandsWithIndex[commandsCompleted + 1].commandIndex;

            // Adds all the commands in order according to the index
            // All the commands (which have priority at the same value compared to
            // trajectory)
            int earlyCommandsCompleted = 0;

            for (commandWithIndex cmndWithIndex : commandsWithIndex) {
                if (cmndWithIndex.commandIndex == targetIndex) {
                    seqCommandGroup.addCommands(cmndWithIndex.command);
                    earlyCommandsCompleted++;
                }
            }

            commandsCompleted += earlyCommandsCompleted;

            partialTrajList.clear();
            for (int i = previousTargetIndex - 1; i < targetIndex; i++) {
                partialTrajList.add(trajectoryList.get((i <= 0) ? 0 : i)); // To prevent it from trying to call -1
            }

            trajStatesCompelted += targetIndex - previousTargetIndex;

            seqCommandGroup.addCommands(new AutoDriveTrajectory(driveBase, new Trajectory(partialTrajList), StopMotors.stop, Brakes.on));

            if (commandsCompleted == commandsWithIndex.length && trajStatesCompelted == trajectoryList.size()) continue;

            else if (commandsCompleted == commandsWithIndex.length) {
                partialTrajList.clear();
                for (int i = previousTargetIndex - 1; i < trajectoryList.size() - 1; i++) {
                    partialTrajList.add(trajectoryList.get(i));
                }

                seqCommandGroup.addCommands(new AutoDriveTrajectory(driveBase, new Trajectory(partialTrajList), StopMotors.stop, Brakes.on));

                continue;
            }

            else if (trajStatesCompelted == trajectoryList.size()) {
                for (int i = targetIndex + 1; i < commandsWithIndex.length - 1; i++) {
                    seqCommandGroup.addCommands(commandsWithIndex[i].command);

                    continue;
                }
            }
        }

        seqCommandGroup.schedule();
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