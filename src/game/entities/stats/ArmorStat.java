/*
 * Jerry Kim (18015036), 2019
 */
package game.entities.stats;

import game.entities.Stat;

/**
 *
 * @author jerrykim
 */
public class ArmorStat extends Stat {
    
    public ArmorStat()
    {
        super("Armor");
    }
    
    public @Override double GetValue() { return 0.2 * Math.pow(Level.GetValue(), 1.5) + 5; }
    
    public @Override String GetDescription() { return String.format("Reduces incoming damage by %.2f.", GetValue()); }
}
