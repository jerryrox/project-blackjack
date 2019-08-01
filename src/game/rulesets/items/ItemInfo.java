/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Informational representation of an item.
 * This serves like a metadata for an item instance.
 * @author jerrykim
 */
public class ItemInfo {
    
    /**
     * Identifier of the item.
     */
    public final int Id;
    
    /**
     * Grade of the item.
     */
    public final ItemGrade Grade;
    
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
    
    
    public ItemInfo(int id, ItemGrade grade, String name, String description,
            int buyCost, int sellPrice, int duration)
    {
        this.Id = id;
        this.Grade = grade;
        this.Name = name;
        this.Description = description;
        this.BuyCost = buyCost;
        this.SellPrice = sellPrice;
        this.Duration = duration;
    }
    
    /**
     * Applies item effects to game.
     * @param player
     * @param item 
     */
    public void ApplyToGame(GamePlayer player, GameItem item) {}
}
