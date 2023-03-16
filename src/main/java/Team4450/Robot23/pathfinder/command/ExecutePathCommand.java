package Team4450.Robot23.pathfinder.command;

import Team4450.Lib.Util;
import Team4450.Robot23.pathfinder.Path;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class ExecutePathCommand extends CommandBase
{

    private Path<?> path;
    
    public ExecutePathCommand(Path<?> path)
    {
        this.path = path;
    }

    @Override
    public void initialize()
    {
        Util.consoleLog("Scheduled!");
        SequentialCommandGroup commands = path.group();
        commands.schedule();
    }
}
