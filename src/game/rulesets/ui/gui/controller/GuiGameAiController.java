/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui.controller;

import game.rulesets.AiBehavior;
import game.rulesets.Deck;
import game.rulesets.GameAIPlayer;
import game.rulesets.GamePlayer;
import game.rulesets.ui.gui.GuiRuleset;
import game.utils.Random;
import javax.swing.Timer;

/**
 * Specialized game controller for AI.
 * @author jerrykim
 */
public class GuiGameAiController extends GuiGameController {
    
    private AiBehavior aiBehavior;
    
    private Timer actionTimer;
    
    
    public GuiGameAiController(GuiRuleset rulesetDisplay, GameAIPlayer player,
            GamePlayer human, Deck deck)
    {
        super(rulesetDisplay, player);
        aiBehavior = new AiBehavior(player, human, deck);
    }
    
    public @Override void Dispose()
    {
        if(actionTimer != null)
            actionTimer.stop();
        actionTimer = null;
    }
    
    public @Override void SetTurn(boolean onTurn)
    {
        if(onTurn)
        {
            // Simulate thinking time.
            Timer timer = new Timer(Random.Range(750, 1500), (e) -> {
                DoAiAction();
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
    
    /**
     * Performs the actual actions of AI.
     */
    private void DoAiAction()
    {
        if(aiBehavior.WillDraw())
            Draw();
        else
            Skip();
    }
}
