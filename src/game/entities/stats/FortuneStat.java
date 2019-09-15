/*
 * Jerry Kim (18015036), 2019
 */
package game.entities.stats;

import game.entities.Stat;

/**
 *
 * @author jerrykim
 */
public class FortuneStat extends Stat {

    public FortuneStat()
    {
        super("Fortune");
    }
    
    public @Override double GetValue() { return Level.GetValue() * 0.15 + 1; }

    public @Override String GetDescription() { return String.format("Multiplies rewards by %d%%.", (int)(GetValue() * 100)); }
    
}
