/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.data.Yieldable;
import game.entities.OwnedItemModel;
import game.io.IStorage;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;

/**
 * Store which manages the user's owned items.
 * @author jerrykim
 */
public class ItemStore {
    
    /**
     * Storage from which the items are saved/loaded.
     */
    private IStorage<OwnedItemModel> storage;
    
    /**
     * Definitions of items for static item information.
     */
    private ItemDefinitions definitions;
    
    
    public ItemStore(ItemDefinitions definitions, IStorage<OwnedItemModel> storage)
    {
        this.definitions = definitions;
        this.storage = storage;
        storage.Initialize();
    }
    
    /**
     * Adds a new owned item instance for specified info.
     * @param item 
     */
    public void AddItem(ItemInfo item) { storage.Add(new OwnedItemModel(definitions, item)); }
    
    /**
     * Removes the specified item from storage.
     * @param item 
     */
    public void RemoveItem(OwnedItemModel item) { storage.Remove(item.GetId()); }
    
    /**
     * Removes an arbitrary owned item instance that refers to specified item info.
     * @param item 
     */
    public void RemoveItem(ItemInfo item)
    {
        for(OwnedItemModel i : storage.GetAll())
        {
            if(i.GetInfo() == item)
            {
                storage.Remove(i.GetId());
                return;
            }
        }
    }
    
    /**
     * Returns all items the user currently owns.
     */
    public Iterable<OwnedItemModel> GetItems() { return storage.GetAll(); }
    
    public Iterable<OwnedItemModel> GetItems(ItemInfo item)
    {
        return new Yieldable<OwnedItemModel>(yield -> {
            for(OwnedItemModel i : storage.GetAll())
            {
                if(i.GetInfo() == item)
                    yield.Return(i);
            }
        });
    }
    
    /**
     * Returns the number of items referencing the specified item info.
     * @param item
     */
    public int GetItemCount(ItemInfo item)
    {
        int count = 0;
        for(OwnedItemModel i : storage.GetAll())
        {
            if(i.GetInfo() == item)
                count ++;
        }
        return count;
    }
    
    /**
     * Saves all entries to storage.
     */
    public void Save() { storage.Save(); }
}
