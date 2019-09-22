/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.data.IDisposable;
import game.data.Yieldable;
import game.entities.OwnedItemEntity;
import game.io.IStorage;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;

/**
 * Store which manages the user's owned items.
 * @author jerrykim
 */
public class ItemStore implements IDisposable {
    
    /**
     * Storage from which the items are saved/loaded.
     */
    private IStorage<OwnedItemEntity> storage;
    
    /**
     * Definitions of items for static item information.
     */
    private ItemDefinitions definitions;
    
    
    public ItemStore(ItemDefinitions definitions, IStorage<OwnedItemEntity> storage)
    {
        this.definitions = definitions;
        this.storage = storage;
        storage.Initialize();
    }
    
    /**
     * Adds a new owned item instance for specified info.
     * @param item 
     */
    public void AddItem(ItemInfo item) { storage.Add(new OwnedItemEntity(definitions, item)); }
    
    /**
     * Removes the specified item from storage.
     * @param item 
     */
    public void RemoveItem(OwnedItemEntity item) { storage.Remove(item.GetId()); }
    
    /**
     * Removes an arbitrary owned item instance that refers to specified item info.
     * @param item 
     */
    public void RemoveItem(ItemInfo item)
    {
        for(OwnedItemEntity i : storage.GetAll())
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
    public Iterable<OwnedItemEntity> GetItems() { return storage.GetAll(); }
    
    public Iterable<OwnedItemEntity> GetItems(ItemInfo item)
    {
        return new Yieldable<OwnedItemEntity>(yield -> {
            for(OwnedItemEntity i : storage.GetAll())
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
        for(OwnedItemEntity i : storage.GetAll())
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

    public @Override void Dispose()
    {
        storage.Dispose();
    }
}
