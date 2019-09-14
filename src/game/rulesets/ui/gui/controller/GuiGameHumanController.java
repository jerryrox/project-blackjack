/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui.controller;

import game.data.Action;
import game.data.ActionT;
import game.debug.Debug;
import game.rulesets.GamePlayer;
import game.rulesets.items.CloneItemInfo;
import game.rulesets.items.ItemInfo;
import game.rulesets.items.PeekItemInfo;
import game.rulesets.items.ReturnItemInfo;
import game.rulesets.items.RevealItemInfo;
import game.rulesets.ui.gui.GuiRuleset;
import game.ui.gui.UIOverlayController;
import game.ui.gui.objects.customized.UIGameInfoPanel;
import game.ui.gui.overlays.UIUseItemOverlay;

/**
 * Controller specialization for human player.
 * @author jerrykim
 */
public class GuiGameHumanController extends GuiGameController {
    
    private UIGameInfoPanel infoPanel;
    private UIOverlayController overlays;
    
    private ActionT onDraw;
    private ActionT onSkip;
    private ActionT onItem;
    
    
    public GuiGameHumanController(GuiRuleset rulesetDisplay, GamePlayer player,
            UIGameInfoPanel infoPanel, UIOverlayController overlays)
    {
        super(rulesetDisplay, player);
        this.infoPanel = infoPanel;
        this.overlays = overlays;
        
        onDraw = (arg) -> OnDrawButton();
        onSkip = (arg) -> OnSkipButton();
        onItem = (arg) -> OnItemButton();
        
        // Hook on to action button events.
        infoPanel.GetDrawButton().Clicked.Add(onDraw);
        infoPanel.GetSkipButton().Clicked.Add(onSkip);
        infoPanel.GetItemButton().Clicked.Add(onItem);
    }
    
    public @Override void SetTurn(boolean onTurn)
    {
        super.SetTurn(onTurn);
        // Toggle buttons based on human player's turn.
        infoPanel.GetDrawButton().SetActive(onTurn);
        infoPanel.GetSkipButton().SetActive(onTurn);
        infoPanel.GetItemButton().SetActive(onTurn);
    }
    
    public @Override void Dispose()
    {
        infoPanel.GetDrawButton().Clicked.Remove(onDraw);
        infoPanel.GetSkipButton().Clicked.Remove(onSkip);
        infoPanel.GetItemButton().Clicked.Remove(onItem);
    }
    
    private void OnDrawButton()
    {
        if(player.GetHand().GetTotalCardValue() >= 21)
            return;
        Draw();
    }
    
    private void OnSkipButton()
    {
        Skip();
    }
    
    private void OnItemButton()
    {
        UIUseItemOverlay overlay = overlays.ShowView(UIUseItemOverlay.class);
        overlay.SetController(this);
    }
}
