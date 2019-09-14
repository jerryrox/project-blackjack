/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.ui.gui;

import game.data.Action;
import game.data.Events;
import game.debug.Debug;
import game.rulesets.Card;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.PhaseResults;
import java.util.ArrayList;

/**
 * Handles invocation of various game state events and callbacks.
 * @author jerrykim
 */
public class GuiGameStateHandler {
    
    /**
     * List of game state listeners for various game events
     */
    private ArrayList<IGameStateListener> listeners = new ArrayList<>();
    
    /**
     * Name of the event currently waiting for.
     */
    private String eventName;
    
    /**
     * Number of events waiting to be finished.
     */
    private int pendingEvents = 0;
    
    /**
     * Action to be called on event invocation end.
     */
    private Action onEnd;
    
    
    public GuiGameStateHandler()
    {
    }
    
    /**
     * Adds specified listener to list.
     */
    public void AddListener(IGameStateListener listener)
    {
        if(listener != null)
            listeners.add(listener);
    }
    
    /**
     * Sets turn on specified player.
     * Returns whether wait event occurs.
     */
    public boolean InvokeSetTurn(GamePlayer player, Action callback)
    {
        SetCurrentEvent("InvokeSetTurn", callback);
        listeners.forEach(l -> WaitForEvent(l.OnSetTurnState(player)));
        return pendingEvents > 0;
    }
    
    /**
     * Sets turn end for specified player.
     * Returns whether wait event occurs.
     */
    public boolean InvokeTurnEnd(GamePlayer player, Action callback)
    {
        SetCurrentEvent("InvokeTurnEnd", callback);
        listeners.forEach(l -> WaitForEvent(l.OnTurnEndState(player)));
        return pendingEvents > 0;
    }
    
    /**
     * Sets new phase.
     * Returns whether wait event occurs.
     */
    public boolean InvokeNewPhase(Action callback)
    {
        SetCurrentEvent("InvokeNewPhase", callback);
        listeners.forEach(l -> WaitForEvent(l.OnNewPhaseState()));
        return pendingEvents > 0;
    }
    
    /**
     * Sets current phase end.
     * Returns whether wait event occurs.
     */
    public boolean InvokePhaseEnd(Action callback)
    {
        SetCurrentEvent("InvokePhaseEnd", callback);
        listeners.forEach(l -> WaitForEvent(l.OnPhaseEndState()));
        return pendingEvents > 0;
    }
    
    /**
     * Sets evaluation result.
     * Returns whether wait event occurs.
     */
    public boolean InvokeEvaluated(PhaseResults result, int humanDmg, int aiDmg, Action callback)
    {
        SetCurrentEvent("InvokeEvaluated", callback);
        listeners.forEach(l -> WaitForEvent(l.OnEvaluatedState(result, humanDmg, aiDmg)));
        return pendingEvents > 0;
    }
    
    /**
     * Sets draw for specified player and card.
     * Returns whether wait event occurs.
     */
    public boolean InvokeDraw(GamePlayer player, Card card, Action callback)
    {
        SetCurrentEvent("InvokeDraw", callback);
        listeners.forEach(l -> WaitForEvent(l.OnDrawState(player, card)));
        return pendingEvents > 0;
    }
    
    /**
     * Sets skip for specified player.
     * Returns whether wait event occurs.
     */
    public boolean InvokeSkip(GamePlayer player, Action callback)
    {
        SetCurrentEvent("InvokeSkip", callback);
        listeners.forEach(l -> WaitForEvent(l.OnSkipState(player)));
        return pendingEvents > 0;
    }
    
    /**
     * Sets item use on specified player.
     * Returns whether wait event occurs.
     */
    public boolean InvokeItemUse(GamePlayer player, GameItem item, Action callback)
    {
        SetCurrentEvent("InvokeItemUse", callback);
        listeners.forEach(l -> WaitForEvent(l.OnItemUseState(player, item)));
        return pendingEvents > 0;
    }
    
    private void SetCurrentEvent(String name, Action callback)
    {
        Debug.Log("GuiGameStatehandler.SetCurrentEvent - Waiting for event: " + name);
        onEnd = callback;
        eventName = name;
    }
    
    private void WaitForEvent(Events event)
    {
        if(event != null)
        {
            event.Add((arg) -> {
                pendingEvents --;
                if(pendingEvents == 0)
                    InvokeOnEnd();
            });
            pendingEvents ++;
        }
    }
    
    private void InvokeOnEnd()
    {
        Action action = onEnd;
        onEnd = null;
        if(action != null)
            action.Invoke();
    }
}
