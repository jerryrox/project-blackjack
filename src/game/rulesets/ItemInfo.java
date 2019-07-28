/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

/**
 * Informational representation of an item.
 * This serves like a metadata for an item instance.
 * @author jerrykim
 */
public final class ItemInfo {
    
    /**
     * Identifier of the item.
     */
    public final int Id;
    
    /**
     * Type of the item.
     */
    public final ItemTypes Type;
    
    /**
     * Name of the item.
     */
    public final String Name;
    
    /**
     * Description of the item.
     */
    public final String Description;
    
    /**
     * Cost of the item when bought at shop.
     */
    public final int BuyCost;
    
    /**
     * Price of the item when sold.
     */
    public final int SellPrice;
    
    /**
     * Duration of the item effect.
     */
    public final int Duration;
    
    
    public ItemInfo(int id, ItemTypes type, String name, String description,
            int buyCost, int sellPrice, int duration)
    {
        this.Id = id;
        this.Type = type;
        this.Name = name;
        this.Description = description;
        this.BuyCost = buyCost;
        this.SellPrice = sellPrice;
        this.Duration = duration;
    }
}
