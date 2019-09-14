/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.data.Events;
import game.data.Vector2;
import game.rulesets.Card;
import game.rulesets.Deck;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.rulesets.PhaseResults;
import game.rulesets.items.PeekItemInfo;
import game.rulesets.items.ReturnItemInfo;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.objects.UIObject;

/**
 * Deck displayer in game info panel.
 * @author jerrykim
 */
public class UIGameDeck extends UIObject implements IGameStateListener {
    
    public static final float CardScale = 0.6f;
    
    private UILabel nextLabel;
    
    private GameProcessor gameProcessor;
    
    /**
     * The top card in the deck.
     */
    private UICardDisplayer topCard;
    
    private boolean isPeek = false;
    
    
    public UIGameDeck()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        UILabel title = CreateChild().AddComponent(new UILabel());
        {
            title.SetText("Deck");
            title.SetFontSize(20);
        }
        
        final float cardsPos = 135;
        for(int i=0; i<3; i++)
        {
            UICardDisplayer card = AddChild(new UICardDisplayer());
            card.SetDepth(3 - i);
            card.GetTransform().SetLocalPosition((i-1) * 3, (i-1) * 3 + cardsPos);
            card.SetScale(CardScale);
            
            if(i == 0)
                topCard = card;
        }
        
        nextLabel = CreateChild().AddComponent(new UILabel());
        {
            nextLabel.SetFontSize(16);
            nextLabel.GetTransform().SetLocalPosition(0, 270);
            nextLabel.SetText("Next card: ??");
        }
    }
    
    /**
     * Sets the game processor managing current session.
     */
    public void SetGameProcessor(GameProcessor gameProcessor)
    {
        this.gameProcessor = gameProcessor;
    }
    
    /**
     * Returns the world position of the first card in the deck.
     */
    public Vector2 GetCardPosition()
    {
        return topCard.GetTransform().GetWorldPosition();
    }
    
    /**
     * Sets whether the next card should be peeked at.
     * @param isPeek 
     */
    public void SetPeek(boolean isPeek)
    {
        if(this.isPeek == isPeek)
            return;
        this.isPeek = isPeek;
        
        if(isPeek)
            nextLabel.SetText("Next card: " + gameProcessor.GetDeck().PeekCard().GetNumber());
        else
            nextLabel.SetText("Next card: ??");
    }
    
    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player) 
    {
        // Remove peek state
        SetPeek(false);
        return null;
    }

    public @Override Events OnNewPhaseState() { return null; }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg) { return null; }

    public @Override Events OnDrawState(GamePlayer player, Card card) { return null; }

    public @Override Events OnSkipState(GamePlayer player) { return null; }

    public @Override Events OnItemUseState(GamePlayer player, GameItem item)
    {
        // Set peeking
        if(player.IsHuman())
        {
            if(item.Info instanceof PeekItemInfo)
                SetPeek(true);
            else if(item.Info instanceof ReturnItemInfo)
                SetPeek(false);
        }
        return null;
    }
    
    
}
