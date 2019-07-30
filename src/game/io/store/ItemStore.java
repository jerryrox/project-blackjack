/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.data.Yieldable;
import game.entities.OwnedItem;
import game.io.IStorage;
import game.rulesets.ItemDefinitions;
import game.rulesets.ItemInfo;

/**
 * Store which manages the user's owned items.
 * @author jerrykim
 */
public class ItemStore {
    
    /**
     * Storage from which the items are saved/loaded.
     */
    private IStorage<OwnedItem> storage;
    
    /**
     * Definitions of items for static item information.
     */
    private ItemDefinitions definitions;
    
    
    public ItemStore(ItemDefinitions definitions, IStorage<OwnedItem> storage)
    {
        this.definitions = definitions;
        this.storage = storage;
    }
    
    /**
     * Adds a new owned item instance for specified info.
     * @param item 
     */
    public void AddItem(ItemInfo item) { storage.Add(new OwnedItem(definitions, item)); }
    
    /**
     * Removes the specified item from storage.
     * @param item 
     */
    public void RemoveItem(OwnedItem item) { storage.Remove(item.GetId()); }
    
    /**
     * Returns all items the user currently owns.
     */
    public Iterable<OwnedItem> GetItems() { return storage.GetAll(); }
}
