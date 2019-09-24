/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.data.Func;
import game.database.DatabaseConnection;
import game.debug.ConsoleLogger;
import game.debug.Debug;
import game.debug.ILogger;
import game.io.storage.ItemDatabaseStorage;
import game.io.storage.ItemFileStorage;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class ItemStoreIT {
    
    /**
     * Testing item store with a file system storage backend.
     */
    @Test
    public void TestWithFileSystem()
    {
        Debug.Initialize(new ConsoleLogger());
        ItemDefinitions definitions = new ItemDefinitions();
        
        Test(definitions, () -> new ItemStore(definitions, new ItemFileStorage(definitions)));
    }
    
    /**
     * Testing item store with a database storage backend.
     */
    @Test
    public void TestWithDatabase()
    {
        ILogger logger = new ConsoleLogger();
        Debug.Initialize(logger);
        ItemDefinitions definitions = new ItemDefinitions();
        DatabaseConnection connection = new DatabaseConnection("ItemStoreITDB", logger);
        
        Test(definitions, () -> new ItemStore(definitions, new ItemDatabaseStorage(connection, definitions)));
    }
    
    private void Test(ItemDefinitions definitions, Func<ItemStore> createStore)
    {
        ItemStore store = createStore.Invoke();
        
        ItemInfo targetInfo = definitions.AgilityUpSSS;
        
        // Add item
        int curCount = store.GetItemCount(targetInfo);
        store.AddItem(targetInfo);
        
        assertEquals(curCount + 1, store.GetItemCount(targetInfo));
        
        // Remove item
        store.RemoveItem(targetInfo);
        
        assertEquals(curCount, store.GetItemCount(targetInfo));
        
        // Add another item
        targetInfo = definitions.Clone;
        curCount = store.GetItemCount(targetInfo);
        store.AddItem(targetInfo);
        
        assertEquals(curCount + 1, store.GetItemCount(targetInfo));
        
        // Save and dispose
        store.Save();
        store.Dispose();
        
        // Reload
        store = createStore.Invoke();
        
        // Check if count still matches
        assertEquals(curCount + 1, store.GetItemCount(targetInfo));
    }
}
