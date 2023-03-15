package Team4450.Robot23.pathfinder;

import java.util.Iterator;
import java.util.List;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Path<T extends State<T, ?>> implements Iterable<T> {

    private List<T> states;
    private Translation2d start;

    private CommandSupplier<T> command;

    /**
     * Constructs a path.
     * @param command Supplier of commands for command group construction.
     * @param start Start position.
     * @param states List of states to execute.
     */
    public Path(CommandSupplier<T> command, Translation2d start, List<T> states)
    {
        this.command = command;
        this.states = states;
        this.start = start;
    }

    /**
     * Gets the starting position of the path.
     * @return Starting position of the path.
     */
    public Translation2d start()
    {
        return start;
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
    public Iterator<T> iterator() {
        return states.iterator();
    }
    
    /**
     * Constructs a sequential command group from the path.
     * @return Instance of SequentialCommandGroup with path's states and rotation.
     */
    public SequentialCommandGroup group()
    {
        SequentialCommandGroup group = new SequentialCommandGroup();
        for (int i = 0; i < states.size() - 1; i++)
        {
            group.addCommands(command.construct(states.get(i)));
        }
        return group;
    }

    /**
     * Builder class for paths.
     */
    public static class Builder<T extends State<T, ?>>
    {

        private List<T> states;
        private Translation2d start;

        CommandSupplier<T> command;

        /**
         * Constructs a builder with one state.
         * @param start Beginning of path.
         * @param end First state.
         */
        public Builder(Translation2d start, T end)
        {
            this.start = start;
            this.states.add(end);
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
         * Constructs the path instance.
         * @return A new path instance with specified parameters.
         */
        public Path<T> build()
        {
            return new Path<T>(command, start, states);
        }
    }
}
