/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.data.Action;
import game.entities.Stat;
import game.rulesets.items.ItemInfo;
import game.ui.gui.UIOverlayController;

/**
 * Provides presetting of UIDialogOverlays for convenience.
 * @author jerrykim
 */
public final class DialogPresetter {
    
    public static UIDialogOverlay NotEnoughGold(UIOverlayController overlay, Action callback)
    {
        UIDialogOverlay dialog = overlay.ShowView(UIDialogOverlay.class);
        dialog.SetYesMode("You don't have enough gold.", callback);
        return dialog;
    }
    
    public static UIDialogOverlay ConfirmBuyItem(UIOverlayController overlay, ItemInfo item, Action onYes, Action onNo)
    {
        UIDialogOverlay dialog = overlay.ShowView(UIDialogOverlay.class);
        dialog.SetYesNoMode(
            String.format("Would you like to buy %s for %d gold?", item.Name, item.BuyCost),
            onYes, onNo
        );
        return dialog;
    }
    
    public static UIDialogOverlay ConfirmUpgradeStat(UIOverlayController overlay, Stat stat, Action onYes, Action onNo)
    {
        UIDialogOverlay dialog = overlay.ShowView(UIDialogOverlay.class);
        dialog.SetYesNoMode(
            String.format("Would you like to upgrade %s stat to level %d?", stat.GetName(), stat.Level.GetValue() + 1),
            onYes, onNo
        );
        return dialog;
    }
}
