/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.data.BindableInt;
import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Representation of a single stat value of the user.
 * @author jerrykim
 */
public abstract class Stat implements IKeyValueSerializable {
    
    public final BindableInt Level = new BindableInt(0);
    
    protected String name;
    
    
    protected Stat(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns the name of the stat.
     */
    public String GetName() { return name; }
    
    /**
     * Returns the cost to upgrading this stat at current level.
     */
    public int GetCost() { return 50 + Level.GetValue() * 40; }
    
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
        serializer.Set(name + "_level", Level.GetValue());
    }
    
    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        Level.SetValue(serializer.GetInt(name + "_level"));
    }
}
