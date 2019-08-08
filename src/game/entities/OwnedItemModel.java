/*
 * Jerry Kim (18015036), 2019
 */
package game.entities;

import game.io.IKeyValueSerializable;
import game.io.serializers.KeyValueSerializer;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;

/**
 * Item instance owned by the user.
 * @author jerrykim
 */
public class OwnedItemModel implements IKeyValueSerializable, IEntity {
    
    /**
     * Identifier of the item.
     */
    private String id;
    
    /**
     * The item definition this instance refers.
     */
    private ItemInfo itemInfo;
    
    /**
     * Definitions of item informations.
     */
    private ItemDefinitions definitions;

    
    public OwnedItemModel(ItemDefinitions definitions)
    {
        this.definitions = definitions;
    }
    
    public OwnedItemModel(ItemDefinitions definitions, ItemInfo info)
    {
        this.definitions = definitions;
        this.itemInfo = info;
    }
    
    public @Override void SetId(String id) { this.id = id; }

    public @Override String GetId() { return id; }
    
    public ItemInfo GetInfo() { return itemInfo; }
    
    public @Override void Serialize(KeyValueSerializer serializer)
    {
        serializer.Set("infoId", itemInfo.Id);
    }

    public @Override void Deserialize(KeyValueSerializer serializer)
    {
        itemInfo = definitions.GetInfo(serializer.GetInt("infoId"));
    }
}
