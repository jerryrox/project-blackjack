/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui.controller;

import game.data.Action;
import game.rulesets.GamePlayer;
import game.rulesets.items.ItemInfo;
import game.rulesets.ui.gui.GuiRuleset;

/**
 * Base controller instance for interacting with the game.
 * @author jerrykim
 */
public class GuiGameController {
    
    protected GuiRuleset rulesetDisplay;
    protected GamePlayer player;
    
    
    
    public GuiGameController(GuiRuleset rulesetDisplay, GamePlayer player)
    {
        this.rulesetDisplay = rulesetDisplay;
        this.player = player;
    }
    
    /**
     * Disposes whatever temporary data used during this session.
     */
    public void Dispose() {}
    
    /**
     * Sets whether this player is on its turn.
     */
    public void SetTurn(boolean onTurn) {}
    
    /**
     * Performs draw action.
     */
    public void Draw()
    {
        rulesetDisplay.SetDraw(player);
    }
    
    /**
     * Performs skip action.
     */
    public void Skip()
    {
        rulesetDisplay.SetSkip(player);
    }
    
    /**
     * Performs item use action.
     */
    public void UseItem(ItemInfo item, Action callback)
    {
        // Apply item on player.
        rulesetDisplay.SetItemUse(player, item, callback);
    }
}
