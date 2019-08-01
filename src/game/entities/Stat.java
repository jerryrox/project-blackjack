/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Representation of a single stat value of the user.
 * @author jerrykim
 */
public abstract class Stat implements IKeyValueSerializable {
    
    protected String name;
    protected int level;
    
    
    protected Stat(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the name of the stat.
     */
    public String GetName() { return name; }
    
    /**
     * Returns the level of the stat.
     */
    public int GetLevel() { return level; }
    
    /**
     * Sets the level of the stat.
     * @param level 
     */
    public void SetLevel(int level) { this.level = level; }
    
    /**
     * Returns the cost to upgrading this stat at current level.
     */
    public int GetCost() { return 50 + level * 40; }
    
    /**
     * Returns the evaluated value derived from stat level.
     */
    public abstract double GetValue();
    
    /**
     * Returns the description of the stat.
     */
    public abstract String GetDescription();
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        serializer.Set(name + "_level", level);
    }
    
    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        level = serializer.GetInt(name + "_level");
    }
}
