/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui;

import game.data.Events;
import game.rulesets.Card;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.PhaseResults;

/**
 * Indicates that the object responds to game events happening during game.
 * All event methods may return an Events object which promises to invoke upon
 * completion of time-consuming processes such as animations.
 * @author jerrykim
 */
public interface IGameStateListener {
    
    /**
     * Event called when the specified player's turn has arrived.
     */
    Events OnSetTurnState(GamePlayer player);
    
    /**
     * Event called when the specified player has ended its turn.
     */
    Events OnTurnEndState(GamePlayer player);
    
    /**
     * Event called when a new phase has started.
     */
    Events OnNewPhaseState();
    
    /**
     * Event called when the current phase is about to end.
     */
    Events OnPhaseEndState();
    
    /**
     * Event called when current phase has been evaluated.
     * humanDmg is how much damage human player has dealt to the AI.
     * aiDmg is how much damage ai player has dealt to the Human.
     */
    Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg);
    
    /**
     * Event called when the specified player has requested drawing of a card.
     */
    Events OnDrawState(GamePlayer player, Card card);
    
    /**
     * Event called when the specified player has requested skipping.
     */
    Events OnSkipState(GamePlayer player);
    
    /**
     * Event called when the specified item has been applied on player.
     */
    Events OnItemUseState(GamePlayer player, GameItem item);
}
