/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.OwnedItem;
import game.rulesets.ItemDefinitions;

/**
 * FileSystemStorage extension for owned item data.
 * @author jerrykim
 */
public class ItemFileStorage extends FileSystemStorage<OwnedItem> {
    
    public ItemFileStorage(ItemDefinitions definitions)
    {
        super("items.data", () -> new OwnedItem(definitions));
    }
    
}
