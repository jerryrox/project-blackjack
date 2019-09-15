/*
 * Jerry Kim (18015036), 2019
 */
package game.entities.stats;

import game.entities.Stat;

/**
 *
 * @author jerrykim
 */
public class PowerStat extends Stat {

    public PowerStat()
    {
        super("Power");
    }
    
    public @Override double GetValue() { return 0.5 * Math.pow(Level.GetValue(), 1.5) + 10; }

    public @Override String GetDescription() { return String.format("Grants %.2f attack damage.", GetValue()); }
}
