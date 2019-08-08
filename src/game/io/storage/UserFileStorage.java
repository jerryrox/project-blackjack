/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.UserModel;

/**
 * FileSystemStorage extension for user data.
 * @author jerrykim
 */
public class UserFileStorage extends FileSystemStorage<UserModel> {
    
    public UserFileStorage()
    {
        super("user.data", () -> new UserModel());
    }
}
