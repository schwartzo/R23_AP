package Team4450.Robot23.pathfinder;

import Team4450.Robot23.pathfinder.math.Vertex2d;
import edu.wpi.first.math.geometry.Translation2d;

public class StateTranslation2d extends Translation2d implements State2d<StateTranslation2d>
{

    public StateTranslation2d(Translation2d other)
    {
        super(other.getX(), other.getY());
    }

    @Override
    public StateTranslation2d copy()
    {
        return new StateTranslation2d(this);
    }

    @Override
    public StateTranslation2d copy(Translation2d other)
    {
        return new StateTranslation2d(other);
    }

    @Override
    public StateTranslation2d copy(Vertex2d other) {
        return new StateTranslation2d(new Translation2d(other.getX(), other.getY()));
    }

    @Override
    public StateTranslation2d plus(StateTranslation2d other)
    {
        return new StateTranslation2d(this.plus(new Translation2d(other.getX(), other.getY())));
    }

    @Override
    public StateTranslation2d minus(StateTranslation2d other)
    {
        return new StateTranslation2d(this.minus(new Translation2d(other.getX(), other.getY())));
    }
}
