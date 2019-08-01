/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.rulesets.items.ItemInfo;

/**
 * Item used in game.
 * @author jerrykim
 */
public class GameItem {
    
    public final ItemInfo Info;
    
    private int duration;
    private double damageMult;
    private double armorMult;
    private double critChanceMult;
    private double critResistMult;
    
    
    public GameItem(ItemInfo info)
    {
        Info = info;
        duration = info.Duration;
    }
    
    public int GetDuration() { return duration; }
    public void SetDuration(int duration) { this.duration = duration; }
    
    public double GetDamageMult() { return damageMult; }
    public void SetDamageMult(double damageMult) { this.damageMult = damageMult; }
    
    public double GetArmorMult() { return armorMult; }
    public void SetArmorMult(double armorMult) { this.armorMult = armorMult; }
    
    public double GetCritChanceMult() { return critChanceMult; }
    public void SetCritChanceMult(double critChanceMult) { this.critChanceMult = critChanceMult; }
    
    public double GetCritResistMult() { return critResistMult; }
    public void SetCritResistMult(double critResistMult) { this.critResistMult = critResistMult; }
}
