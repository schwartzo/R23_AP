package Team4450.Robot23.pathfinder;

import java.util.Iterator;
import java.util.List;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Path implements Iterable<Translation2d> {

    private List<Translation2d> translations;
    private Rotation2d rotation;
    private Translation2d start;

    private TranslationCommand tlCommand;
    private TransformCommand trCommand;

    /**
     * Constructs a path.
     * @param tlCommand Translation command.
     * @param trCommand Transform command.
     * @param start Start position.
     * @param rotation Path final rotation value.
     * @param translations List of translations to execute.
     */
    public Path(TranslationCommand tlCommand, TransformCommand trCommand, Translation2d start, Rotation2d rotation, List<Translation2d> translations)
    {
        this.tlCommand = tlCommand;
        this.trCommand = trCommand;
        this.translations = translations;
        this.rotation = rotation;
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
     * Gets a translation by index.
     * @param i Index of the translation.
     * @return Translation at the specified index.
     */
    public Translation2d get(int i)
    {
        return translations.get(i);
    }

    /**
     * Removes a translation by index.
     * @param i Index of the translation to remove.
     */
    public void remove(int i)
    {
        translations.remove(i);
    }

    /**
     * Adds a translation at an index.
     * @param i Index to add the translation.
     * @param t Translation to add.
     */
    public void add(int i, Translation2d t)
    {
        translations.add(i, t);
    }

    /**
     * Adds a translation at the back of the list.
     * @param t Translation to add.
     */
    public void add(Translation2d t)
    {
        translations.add(t);
    }

    /**
     * Adds all translations in a list at an index.
     * @param i Index to add at.
     * @param t Translations to add.
     */
    public void addAll(int i, List<Translation2d> t)
    {
        translations.addAll(i, t);
    }

    /**
     * Adds all translations in a list at the back of the list.
     * @param t Translations to add.
     */
    public void addAll(List<Translation2d> t)
    {
        translations.addAll(t);
    }

    /**
     * Adds all translations in a path at an index.
     * @param i Index to add at.
     * @param t Path to add.
     */
    public void addAll(int i, Path p)
    {
        translations.addAll(i, p.translations);
    }

    /**
     * Adds all translations in a path at the back of the list.
     * @param p Path to add.
     */
    public void addAll(Path p)
    {
        translations.addAll(p.translations);
    }

    /**
     * Gets the size of the translation list.
     * @return Size of the translation list.
     */
    public int size()
    {
        return translations.size();
    }

    @Override
    public Iterator<Translation2d> iterator() {
        return translations.iterator();
    }
    
    /**
     * Constructs a sequential command group from the path.
     * @return Instance of SequentialCommandGroup with path's translations and rotation.
     */
    public SequentialCommandGroup group()
    {
        SequentialCommandGroup group = new SequentialCommandGroup();
        for (int i = 0; i < translations.size() - 1; i++)
        {
            group.addCommands(tlCommand.construct(translations.get(i)));
        }
        group.addCommands(trCommand.construct(new Transform2d(translations.get(translations.size() - 1), rotation)));
        return group;
    }

    /**
     * Builder class for paths.
     */
    public static class Builder
    {

        private List<Translation2d> translations;
        private Rotation2d rotation;
        private Translation2d start;

        private TranslationCommand tlCommand;
        private TransformCommand trCommand;

        /**
         * Constructs a builder with one translation.
         * @param start Beginning of translation.
         * @param end End of translation.
         */
        public Builder(Translation2d start, Translation2d end)
        {
            this.start = start;
            this.translations.add(end);
        }

        /**
         * Sets the commands for command group construction.
         * @param tlCommand Translation command.
         * @param trCommand Transform command.
         * @return Updated builder instance.
         */
        public Builder commands(TranslationCommand tlCommand, TransformCommand trCommand)
        {
            this.tlCommand = tlCommand;
            this.trCommand = trCommand;
            return this;
        }

        /**
         * Adds a translation to the path.
         * @param point Translation to add.
         * @return Updated builder instance.
         */
        public Builder add(Translation2d point)
        {
            translations.add(point);
            return this;
        }

        /**
         * Sets rotation for the path.
         * @param rot Rotation value.
         * @return Updated builder instance.
         */
        public Builder rotation(Rotation2d rot)
        {
            this.rotation = rot;
            return this;
        }

        /**
         * Constructs the path instance.
         * @return A new path instance with specified parameters.
         */
        public Path build()
        {
            return new Path(tlCommand, trCommand, start, rotation, translations);
        }
    }
}
