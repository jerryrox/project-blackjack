/*
 * Jerry Kim (18015036), 2019
 */
package game.entities.stats;

import game.entities.Stat;

/**
 *
 * @author jerrykim
 */
public class EnduranceStat extends Stat {
    
    public EnduranceStat()
    {
        super("Endurance");
    }
    
    public @Override double GetValue() { return 1.5 * Math.pow(level, 1.8) + 25; }
    
    public @Override String GetDescription() { return String.format("Grants %d max health.", (int)GetValue()); }
}
