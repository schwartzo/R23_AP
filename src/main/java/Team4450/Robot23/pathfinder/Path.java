package Team4450.Robot23.pathfinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Team4450.Robot23.pathfinder.math.Vertex2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Path<T extends State<T>> implements Iterable<T>
{

    private List<T> states;
    private Vertex2d start;

    private CommandSupplier<T> command;

    /**
     * Constructs a path.
     * @param command Supplier of commands for command group construction.
     * @param start Start position.
     * @param states List of states to execute.
     */
    public Path(CommandSupplier<T> command, Vertex2d start, List<T> states)
    {
        this.command = command;
        this.states = states;
        this.start = start;
    }

    /**
     * Constructs a new path from another path.
     * @param original Path to copy from.
     */
    public Path(Path<T> original)
    {
        this.states = new ArrayList<T>(original.states);
        this.start = original.start;
        this.command = original.command;
    }

    /**
     * Gets the starting position of the path.
     * @return Starting position of the path.
     */
    public Vertex2d start()
    {
        return start;
    }

    /**
     * Gets the list of states used by the path.
     * @return The list of states used by the path.
     */
    public List<T> states()
    {
        return states;
    }

    /**
     * Gets the command supplier.
     * @return The command supplier.
     */
    public CommandSupplier<T> command()
    {
        return command;
    }

    /**
     * Gets a state by index.
     * @param i Index of the state.
     * @return State at the specified index.
     */
    public T get(int i)
    {
        return states.get(i);
    }

    /**
     * Removes a state by index.
     * @param i Index of the state to remove.
     */
    public void remove(int i)
    {
        states.remove(i);
    }

    /**
     * Adds a state at an index.
     * @param i Index to add the state.
     * @param t State to add.
     */
    public void add(int i, T t)
    {
        states.add(i, t);
    }

    /**
     * Adds a state at the back of the list.
     * @param t State to add.
     */
    public void add(T t)
    {
        states.add(t);
    }

    /**
     * Adds all states in a list at an index.
     * @param i Index to add at.
     * @param t States to add.
     */
    public void addAll(int i, List<T> t)
    {
        states.addAll(i, t);
    }

    /**
     * Adds all states in a list at the back of the list.
     * @param t States to add.
     */
    public void addAll(List<T> t)
    {
        states.addAll(t);
    }

    /**
     * Adds all states in a path at an index.
     * @param i Index to add at.
     * @param t Path to add.
     */
    public void addAll(int i, Path<T> p)
    {
        states.addAll(i, p.states);
    }

    /**
     * Adds all states in a path at the back of the list.
     * @param p Path to add.
     */
    public void addAll(Path<T> p)
    {
        states.addAll(p.states);
    }

    /**
     * Gets the size of the state list.
     * @return Size of the state list.
     */
    public int size()
    {
        return states.size();
    }

    @Override
    public Iterator<T> iterator()
    {
        return states.iterator();
    }
    
    /**
     * Adds commands for path execution to a sequential command group.
     * @param group The command group to use.
     */
    public void group(SequentialCommandGroup group)
    {
        for (int i = 0; i < states.size(); i++)
        {
            group.addCommands(command.construct(states.get(i)));
        }
    }

    /**
     * Builder class for paths.
     */
    public static class Builder<T extends State<T>>
    {

        private List<T> states = new ArrayList<>();
        private Vertex2d start;

        CommandSupplier<T> command;

        /**
         * Constructs a builder with one state.
         * @param start Beginning of path.
         * @param end First state.
         */
        public Builder(Vertex2d start, T end)
        {
            this.start = start;
            if (end != null)
                this.states.add(end);
        }
        
        /**
         * Copies non-state parameters from another path. Only command as of now.
         * @param original The path to copy from.
         * @return Updated builder instance.
         */
        public Builder<T> blankFrom(Path<T> original)
        {
            return this.command(original.command());
        }

        /**
         * Sets the commands for command group construction.
         * @param command Supplier of commands.
         * @return Updated builder instance.
         */
        public Builder<T> command(CommandSupplier<T> command)
        {
            this.command = command;
            return this;
        }

        /**
         * Adds a state to the path.
         * @param point State to add.
         * @return Updated builder instance.
         */
        public Builder<T> add(T point)
        {
            states.add(point);
            return this;
        }

        /**
         * Adds a list of states to the path.
         * @param points States to add.
         * @return Updated builder instance.
         */
        public Builder<T> add(List<T> points)
        {
            states.addAll(points);
            return this;
        }

        /**
         * Removes a state from the path.
         * @param i Index of state to remove.
         * @return Updated builder instance.
         */
        public Builder<T> remove(int i)
        {
            states.remove(i);
            return this;
        }

        /**
         * Constructs the path instance.
         * @return A new path instance with specified parameters.
         */
        public Path<T> build()
        {
            return new Path<T>(command, start, states);
        }
    }
}
