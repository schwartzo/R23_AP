package Team4450.Robot23.pathfinder;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class PathExecutor
{

    private Path<?> path;
    
    public PathExecutor(Path<?> path)
    {
        this.path = path;
    }

    public void get(SequentialCommandGroup group)
    {
        path.group(group);
    }
}
