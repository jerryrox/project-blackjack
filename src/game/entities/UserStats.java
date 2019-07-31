/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.entities.stats.ArmorStat;
import game.entities.stats.EnduranceStat;
import game.entities.stats.FortuneStat;
import game.entities.stats.LuckStat;
import game.entities.stats.PowerStat;
import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;

/**
 * Stat information of a user.
 * @author jerrykim
 */
public class UserStats implements IKeyValueSerializable {

    public final Stat Power = new PowerStat();
    public final Stat Armor = new ArmorStat();
    public final Stat Endurance = new EnduranceStat();
    public final Stat Luck = new LuckStat();
    public final Stat Fortune = new FortuneStat();
    
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        Power.Serialize(serializer);
        Armor.Serialize(serializer);
        Endurance.Serialize(serializer);
        Luck.Serialize(serializer);
        Fortune.Serialize(serializer);
    }

    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        Power.Deserialize(serializer);
        Armor.Deserialize(serializer);
        Endurance.Deserialize(serializer);
        Luck.Deserialize(serializer);
        Fortune.Deserialize(serializer);
    }
}
