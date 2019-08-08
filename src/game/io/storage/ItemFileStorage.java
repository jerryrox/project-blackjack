/*
 * Jerry Kim (18015036), 2019
 */
package game.io.storage;

import game.entities.OwnedItemModel;
import game.rulesets.items.ItemDefinitions;

/**
 * FileSystemStorage extension for owned item data.
 * @author jerrykim
 */
public class ItemFileStorage extends FileSystemStorage<OwnedItemModel> {
    
    public ItemFileStorage(ItemDefinitions definitions)
    {
        super("items.data", () -> new OwnedItemModel(definitions));
    }
    
}
