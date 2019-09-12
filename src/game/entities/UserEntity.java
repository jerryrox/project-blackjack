/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.data.Bindable;
import game.data.BindableInt;
import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Representation of a user in the game space.
 * @author jerrykim
 */
public class UserEntity implements IKeyValueSerializable, IEntity {
    
    public final Bindable<String> Id = new Bindable<>(null);
    public final Bindable<String> Username = new Bindable<>(null);
    public final BindableInt Gold = new BindableInt(0);
    public final BindableInt SurvivalRound = new BindableInt(0);
    
    private UserStats stats = new UserStats();
    
    
    public @Override void SetId(String id) { Id.SetValue(id); }
    public @Override String GetId() { return Id.GetValue(); }
    
    public UserStats GetStats() { return stats; }
    
    /**
     * Returns whether this user object is considered an uninitialized, empty data.
     * @return 
     */
    public boolean IsEmptyData() { return Username.GetValue() == null || Username.GetValue().length() == 0; }
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        serializer.Set("username", Username.GetValue());
        serializer.Set("gold", Gold.GetValue());
        serializer.Set("survivalRound", SurvivalRound.GetValue());
        
        stats.Serialize(serializer);
    }

    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        Username.SetValue(serializer.Get("username"));
        Gold.SetValue(serializer.GetInt("gold"));
        SurvivalRound.SetValue(serializer.GetInt("survivalRound"));
        
        stats.Deserialize(serializer);
    }
    
    public @Override String toString()
    {
        return String.format(
            "User - id: %s, username: %s, gold: %d, survivalRound: %d",
            Id.GetValue(),
            Username.GetValue(),
            Gold.GetValue(),
            SurvivalRound.GetValue()
        );
    }
}
