/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.data.Action;
import game.data.ActionT;
import game.rulesets.items.ItemInfo;
import game.ui.gui.components.ui.UIDisplayer;

/**
 * A general item cell displayer interface.
 * @author jerrykim
 */
public interface IUIItemCell {
    
    /**
     * Returns the height of the cell.
     */
    int GetHeight();
    
    /**
     * Returns the panel of the cell.
     */
    UIDisplayer GetPanel();
    
    /**
     * Returns the item info referenced by the cell.
     */
    ItemInfo GetItem();
    
    /**
     * Initializes item display using specified item and value.
     */
    void Setup(ItemInfo item, int value);
    
    /**
     * Sets click callback.
     */
    void SetCallback(Action callback);
}
