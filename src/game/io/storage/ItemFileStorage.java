/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.OwnedItemEntity;
import game.rulesets.items.ItemDefinitions;

/**
 * FileSystemStorage extension for owned item data.
 * @author jerrykim
 */
public class ItemFileStorage extends FileSystemStorage<OwnedItemEntity> {
    
    public ItemFileStorage(ItemDefinitions definitions)
    {
        super("items.data", () -> new OwnedItemEntity(definitions));
    }
    
}
