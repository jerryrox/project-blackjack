/*
 * Jerry Kim (18015036), 2019
 */
package game.entities.stats;

import game.entities.Stat;

/**
 *
 * @author jerrykim
 */
public class LuckStat extends Stat {
    
    public LuckStat()
    {
        super("Luck");
    }
    
    public @Override double GetValue() { return level * 0.5 + 25; }
    
    public @Override String GetDescription() { return String.format("Inflicts a critical hit by %.1f%% chance.", GetValue()); }
}
