/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

/**
 * Provides a general signature for an item instance owned by user.
 * @author jerrykim
 */
public interface IItem {
    
    /**
     * Returns the static information of the item.
     */
    ItemInfo GetInfo();
}
