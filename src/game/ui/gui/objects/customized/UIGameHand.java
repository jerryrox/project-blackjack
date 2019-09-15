/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Events;
import game.data.Vector2;
import game.debug.Debug;
import game.rulesets.Card;
import game.rulesets.GameAIPlayer;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.PhaseResults;
import game.rulesets.PlayerHand;
import game.rulesets.items.CloneItemInfo;
import game.rulesets.items.ReturnItemInfo;
import game.rulesets.items.RevealItemInfo;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.UITransform;
import game.ui.gui.objects.UIObject;
import java.util.ArrayList;

/**
 * Displays cards on the player's hands.
 * @author jerrykim
 */
public class UIGameHand extends UIObject implements IGameStateListener {
    
    private final String DrawCardAniName = "drawcard";
    private final String CardReturnAniName = "cardreturn";
    private final String StartCardAniName = "startcard";
    private final String CloneAniName = "clonecard";
    private final String ReturnAniName = "returnCard";
    
    private final float CardScale = 0.53f;
    private final float MinPos = UICardDisplayer.CardWidth * CardScale / 2;
    private final float MaxPos = 840 - UICardDisplayer.CardWidth * CardScale / 2;
    private final float PosRange = MaxPos - MinPos;
    private final float MaxCardDistance = UICardDisplayer.CardWidth * CardScale + 8;
    
    /**
     * List of cards cached for performance.
     */
    private ArrayList<UICardDisplayer> cards = new ArrayList<>();
    
    /**
     * Local position of the deck to receive card from.
     */
    private Vector2 deckPosition;
    
    /**
     * Number of cards currently active on this hand.
     */
    private int activeCards = 0;
    
    /**
     * Player which this hand displays information for.
     */
    private GamePlayer player;
    
    private UIAnimator animator;
    
    
    public UIGameHand()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        animator = AddComponent(new UIAnimator());
    }
    
    /**
     * Sets the owner player.
     * @param player 
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
    }
    
    /**
     * Sets the deck's world position for animation.
     */
    public void SetDeckPosition(Vector2 pos)
    {
        deckPosition = GetTransform().InverseTransformPoint(pos);
    }
    
    /**
     * Pre-instantiates card displayers and cache them for performance.
     */
    public void PrecookCards()
    {
        if(cards.size() == 0)
        {
            for(int i=0; i<6; i++)
            {
                UICardDisplayer card = CreateCard();
                card.SetActive(false);
            }
        }
    }
    
    /**
     * Disposes view to get ready for next game session.
     */
    public void Dispose()
    {
        for(int i=0; i<cards.size(); i++)
            cards.get(i).SetActive(false);
    }
    
    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player) { return null; }

    public @Override Events OnNewPhaseState()
    {
        // Build animation
        Anime anime = animator.CreateAnime(StartCardAniName);
        float[] positions = CalculateCardPositions(1);
        SetupDrawAnimation(anime, positions[0], player.GetHand().GetCard(0));
        // Play animation
        return PlayAnimation(anime, StartCardAniName);
    }

    public @Override Events OnPhaseEndState()
    {
        // Build card returning animation.
        Anime anime = animator.CreateAnime(CardReturnAniName);
        for(int i=activeCards-1; i>=0; i--)
            SetupReturnAnimation(anime, i, cards.get(i));
        // Play animation.
        return PlayAnimation(anime, CardReturnAniName);
    }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg) { return null; }

    public @Override Events OnDrawState(GamePlayer player, Card card)
    {
        if(player != this.player)
            return null;
        
        // Build animation
        Anime anime = animator.CreateAnime(DrawCardAniName);
        float[] positions = CalculateCardPositions(player.GetHand().GetCardCount());
        SetupShiftAnimation(anime, positions);
        SetupDrawAnimation(anime, positions[positions.length-1], card);
        // Play animation.
        return PlayAnimation(anime, DrawCardAniName);
    }

    public @Override Events OnSkipState(GamePlayer player) { return null; }
    
    public @Override Events OnItemUseState(GamePlayer player, GameItem item)
    {
        // Reveal effect.
        if(player != this.player && item.Info instanceof RevealItemInfo)
            cards.get(0).SetSide(true, true);
        else if(player == this.player)
        {
            // Returning effect.
            if(item.Info instanceof ReturnItemInfo)
            {
                // Build animation
                Anime anime = animator.CreateAnime(ReturnAniName);
                float[] positions = CalculateCardPositions(player.GetHand().GetCardCount());
                SetupShiftAnimation(anime, positions);
                SetupReturnAnimation(anime, activeCards-1, cards.get(activeCards-1));
                // Play animation
                return PlayAnimation(anime, ReturnAniName);
            }
            // Cloning effect.
            else if(item.Info instanceof CloneItemInfo)
            {
                // Use the same routine as draw state.
                return OnDrawState(player, player.GetHand().GetCard(activeCards));
            }
        }
        
        return null;
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        animator.RemoveAnime(DrawCardAniName);
        animator.RemoveAnime(CardReturnAniName);
        animator.RemoveAnime(StartCardAniName);
        
        activeCards = 0;
        for(int i=0; i<cards.size(); i++)
            cards.get(i).SetActive(false);
    }
    
    /**
     * Returns the next available card in list.
     */
    private UICardDisplayer GetNextCard()
    {
        UICardDisplayer card = null;
        if(activeCards >= cards.size())
            card = CreateCard();
        else
        {
            card = cards.get(activeCards);
            card.SetActive(true);
        }
        activeCards ++;
        return card;
    }
    
    /**
     * Creates and returns a new card displayer.
     */
    private UICardDisplayer CreateCard()
    {
        UICardDisplayer card = AddChild(new UICardDisplayer());
        card.GetTransform().SetLocalPosition(deckPosition);
        card.SetScale(CardScale);
        card.SetDepth(cards.size());
        cards.add(card);
        return card;
    }
    
    /**
     * Sets drawing animation on specified anime.
     */
    private void SetupDrawAnimation(Anime anime, float targetPos, Card card)
    {
        // Setup card displayer.
        UICardDisplayer displayer = GetNextCard();
        displayer.SetCard(card);
        
        // Move card from deck to hand.
        UITransform transform = displayer.GetTransform();
        Vector2 startPos = deckPosition;
        Vector2 newPos = new Vector2(startPos);
        
        // Flip to front and position the card at deck if not for AI and first card.
        if(player.IsHuman() || player.GetHand().GetCardCount() != 1)
        {
            anime.AddEvent(0, () -> {
                transform.SetLocalPosition(startPos);
                displayer.SetSide(true, true);
            });
        }
        
        // Add animation frames
        anime.AddSection(0, 30, (progress) -> {
            // Translate
            newPos.X = Easing.QuadEaseOut(progress, startPos.X, targetPos - startPos.X, 0);
            newPos.Y = Easing.QuadEaseOut(progress, startPos.Y, -startPos.Y, 0);
            transform.SetLocalPosition(newPos);
            // Scale
            displayer.SetScale(Easing.Linear(progress, UIGameDeck.CardScale, CardScale - UIGameDeck.CardScale, 0));
        });
    }
    
    /**
     * Sets shifting animation on specified anime.
     */
    private void SetupShiftAnimation(Anime anime, float[] positions)
    {
        int loop = Math.min(positions.length, activeCards);
        for(int i=0; i<loop; i++)
        {
            // Setup variables
            UICardDisplayer displayer = cards.get(i);
            UITransform transform = displayer.GetTransform();
            float startPos = transform.GetLocalPosition().X;
            float targetPos = positions[i];
            int offset = i * 6;
            int endFrame = offset + 30;
            
            // Shift existing card to new position
            anime.AddSection(offset, endFrame, (progress) -> {
                transform.SetLocalPosition(Easing.QuartEaseIn(progress, startPos, targetPos - startPos, 0), 0);
            });
        }
    }
    
    /**
     * Sets card returning animation on specified anime.
     */
    private void SetupReturnAnimation(Anime anime, int index, UICardDisplayer displayer)
    {
        // Setup variables
        UITransform transform = displayer.GetTransform();
        int offset = (activeCards - index - 1) * 6;
        int endFrame = offset + 45;
        // Flip to back side
        anime.AddEvent(offset, () -> {
            displayer.SetSide(false, true);
        });
        // Set animation frames
        anime.AddSection(offset, endFrame, (progress) -> {
            // Translation
            Vector2 pos = transform.GetLocalPosition();
            pos.X = Easing.Linear(progress, pos.X, deckPosition.X - pos.X, 0);
            pos.Y = Easing.Linear(progress, pos.Y, deckPosition.Y - pos.Y, 0);
            transform.SetLocalPosition(pos);
            // Scaling
            displayer.SetScale(Easing.Linear(progress, CardScale, UIGameDeck.CardScale - CardScale, 0));
        });
        anime.AddEvent(endFrame, () -> {
            activeCards --;
            displayer.SetActive(false);
        });
    }
    
    /**
     * Plays animation and returns an event listener object that can subscribe
     * to animation end.
     */
    private Events PlayAnimation(Anime anime, String aniName)
    {
        Events event = new Events();
        // Set event end
        anime.AddEvent(Math.max(anime.GetDuration(), 1), () -> {
            animator.RemoveAnime(aniName);
            event.Invoke(null);
        });
        // Play animation
        anime.Play();
        // Return event.
        return event;
    }
    
    /**
     * Calculates position of all cards.
     */
    private float[] CalculateCardPositions(int cardCount)
    {
        float[] positions = new float[cardCount];
        if(positions.length == 1)
            positions[0] = MinPos;
        else
        {
            float cardDist = Math.min(MaxCardDistance, PosRange / (cardCount - 1));
            for(int i=0; i<positions.length; i++)
                positions[i] = MinPos + i * cardDist;
        }
        return positions;
    }
}
