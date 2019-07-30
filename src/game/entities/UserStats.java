/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Stat information of a user.
 * @author jerrykim
 */
public class UserStats implements IKeyValueSerializable {

    private int powerLevel;
    private int armorLevel;
    private int enduranceLevel;
    private int luckLevel;
    private int fortuneLevel;
    
    
    public int GetPowerLevel() { return powerLevel; }
    public void SetPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }
    
    public int GetArmorLevel() { return armorLevel; }
    public void SetArmorLevel(int armorLevel) { this.armorLevel = armorLevel; }
    
    public int GetEnduranceLevel() { return enduranceLevel; }
    public void SetEndurnaceLevel(int enduranceLevel) { this.enduranceLevel = enduranceLevel; }
    
    public int GetLuckLevel() { return luckLevel; }
    public void SetLuckLevel(int luckLevel) { this.luckLevel = luckLevel; }
    
    public int GetFortuneLevel() { return fortuneLevel; }
    public void SetFortuneLevel(int fortuneLevel) { this.fortuneLevel = fortuneLevel; }
    
    /**
     * Returns the evaluated power derived from power level.
     */
    public double GetPowerValue() { return 0.5 * Math.pow(powerLevel, 1.5) + 10; }
    
    /**
     * Returns the evaluated armor derived from armor level.
     */
    public double GetArmorValue() { return 0.2 * Math.pow(armorLevel, 1.5) + 5; }
    
    /**
     * Returns the evaluated max health derived from endurance level.
     */
    public double GetMaxHealth() { return 1.5 * Math.pow(enduranceLevel, 1.8) + 25; }
    
    /**
     * Returns the evaluated critical chance derived from luck level.
     */
    public double GetCriticalChance() { return luckLevel * 0.5 + 25; }
    
    /**
     * Returns the evaluated fortune derived from fortune level.
     */
    public double GetFortuneValue() { return fortuneLevel * 0.15 + 1; }
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        serializer.Set("powerLevel", powerLevel);
        serializer.Set("armorLevel", armorLevel);
        serializer.Set("enduranceLevel", enduranceLevel);
        serializer.Set("luckLevel", luckLevel);
        serializer.Set("fortuneLevel", fortuneLevel);
    }

    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        powerLevel = serializer.GetInt("powerLevel");
        armorLevel = serializer.GetInt("armorLevel");
        enduranceLevel = serializer.GetInt("enduranceLevel");
        luckLevel = serializer.GetInt("luckLevel");
        fortuneLevel = serializer.GetInt("fortuneLevel");
    }
}
