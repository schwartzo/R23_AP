package Team4450.Robot23.pathfinder;

import edu.wpi.first.math.geometry.Translation2d;

public class STranslation2d extends Translation2d implements State2d<STranslation2d, Translation2d>
{

    public STranslation2d(Translation2d other)
    {
        super(other.getX(), other.getY());
    }

    @Override
    public Translation2d base()
    {
        return this;
    }

    @Override
    public STranslation2d get()
    {
        return this;
    }

    @Override
    public State<STranslation2d, Translation2d> copy()
    {
        return new STranslation2d(this);
    }

    @Override
    public State<STranslation2d, Translation2d> copy(Translation2d modify) {
        return new STranslation2d(modify);
    }

    @Override
    public State<STranslation2d, Translation2d> plus(State<STranslation2d, Translation2d> other) {
        return this.plus(other);
    }

    @Override
    public State<STranslation2d, Translation2d> minus(State<STranslation2d, Translation2d> other) {
        return this.minus(other);
    }
}
