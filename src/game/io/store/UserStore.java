/*
 * Jerry Kim (18015036), 2019
 */
package game.io.store;

import game.debug.Debug;
import game.entities.UserModel;
import game.io.IStorage;

/**
 * Provides access to user information.
 * @author jerrykim
 */
public class UserStore {
    
    /**
     * Source from which the user data is loaded.
     */
    private IStorage<UserModel> storage;
    
    /**
     * Current user loaded and cached from storage.
     */
    private UserModel curUser;
    
    
    public UserStore(IStorage<UserModel> storage)
    {
        this.storage = storage;
        storage.Initialize();
    }
    
    /**
     * Returns the cached user instance loaded from storage.
     */
    public UserModel GetUser() { return curUser; }
    
    /**
     * Loads the user data from storage.
     * A new user instance is returned if doesn't exist.
     */
    public UserModel Load()
    {
        curUser = storage.Get("user");
        if(curUser == null)
            curUser = new UserModel();
        return curUser;
    }
    
    /**
     * Saves current user.
     */
    public void Save()
    {
        storage.Set("user", curUser);
        storage.Save();
    }
}
