/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.data.Func;
import game.database.DatabaseConnection;
import game.debug.ConsoleLogger;
import game.debug.Debug;
import game.debug.ILogger;
import game.entities.UserEntity;
import game.io.storage.DatabaseStorage;
import game.io.storage.FileSystemStorage;
import game.io.storage.UserDatabaseStorage;
import game.io.storage.UserFileStorage;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class UserStoreIT {
    
    /**
     * Testing user store with a file system storage backend.
     */
    @Test
    public void TestWithFileSystem()
    {
        Debug.Initialize(new ConsoleLogger());
        
        Test(() -> new UserStore(new UserFileStorage()));
    }
    
    /**
     * Testing user store with a database storage backend.
     */
    @Test
    public void TestWitDatabase()
    {
        ILogger logger = new ConsoleLogger();
        Debug.Initialize(logger);
        DatabaseConnection connection = new DatabaseConnection("UserStoreITDB", logger);
        
        Test(() -> new UserStore(new UserDatabaseStorage(connection)));
    }
    
    private void Test(Func<UserStore> createStore)
    {
        UserStore store = createStore.Invoke();
        UserEntity user = store.Load();
        assertNotNull(user);
        
        if(user.IsEmptyData())
            user.Username.SetValue("TestUsername");
        
        // Make some change
        int randomGold = (int)(new Random(System.currentTimeMillis()).nextFloat() * 999999999);
        user.Gold.SetValue(randomGold);
        assertEquals(user.Gold.GetValue().intValue(), randomGold);
        
        // Save and dispose store
        store.Save();
        store.Dispose();
        
        // Reset store
        store = createStore.Invoke();
        
        // Reload user
        user = store.Load();
        assertEquals(user.Gold.GetValue().intValue(), randomGold);
    }
}
