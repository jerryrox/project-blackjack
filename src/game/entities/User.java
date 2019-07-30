/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Representation of a user in the game space.
 * @author jerrykim
 */
public class User implements IKeyValueSerializable, IEntity {
    
    private String id;
    private String username;
    private int gold;
    
    private UserStats stats = new UserStats();
    
    
    public @Override void SetId(String id) { this.id = id; }

    public @Override String GetId() { return id; }
    
    public String GetUsername() { return username; }
    public void SetUsername(String username) { this.username = username; }
    
    public int GetGold() { return gold; }
    public void SetGold(int gold) { this.gold = gold; }
    
    public UserStats GetStats() { return stats; }
    
    /**
     * Returns whether this user object is considered an uninitialized, empty data.
     * @return 
     */
    public boolean IsEmptyData() { return username == null || username.length() == 0; }
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        serializer.Set("username", username);
        serializer.Set("gold", gold);
        
        stats.Serialize(serializer);
    }

    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        username = serializer.Get("username");
        gold = serializer.GetInt("gold");
        
        stats.Deserialize(serializer);
    }
    
    public @Override String toString()
    {
        return String.format(
            "User - id: %d, username: %s, gold: %d",
            id,
            username,
            gold
        );
    }
}
