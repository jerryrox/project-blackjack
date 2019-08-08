/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.UserEntity;

/**
 * FileSystemStorage extension for user data.
 * @author jerrykim
 */
public class UserFileStorage extends FileSystemStorage<UserEntity> {
    
    public UserFileStorage()
    {
        super("user.data", () -> new UserEntity());
    }
}
