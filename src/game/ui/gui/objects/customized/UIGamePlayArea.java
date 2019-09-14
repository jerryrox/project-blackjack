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
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.Pivot;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.UITransform;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;
import java.awt.Font;

/**
 * Represents a player's play area in game.
 * @author jerrykim
 */
public class UIGamePlayArea extends UIObject implements IGameStateListener {
    
    private final String ActionAniName = "actionani";
    
    private GamePlayer player;
    private boolean isAi;
    
    private UILabel nameLabel;
    private UIGameHealthbar healthBar;
    private UIGameItemList itemList;
    private UILabel totalLabel;
    private UIGameHand hand;
    private UIGamePhaseResult phaseResulter;
    
    private UISprite actionIcon;
    
    private UIAnimator animator;
    
    private boolean didInvertUi = false;
    
    
    public UIGamePlayArea()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        nameLabel = CreateChild().AddComponent(new UILabel());
        {
            nameLabel.SetPivot(Pivot.Left);
            nameLabel.SetFont(Font.BOLD | Font.ITALIC, 24);
            nameLabel.GetTransform().SetLocalPosition(-446, 42);
        }
        AddChild(healthBar = new UIGameHealthbar());
        {
            healthBar.GetTransform().SetLocalPosition(-216, 42);
        }
        AddChild(itemList = new UIGameItemList());
        {
            itemList.GetTransform().SetLocalPosition(-430, 84);
        }
        totalLabel = CreateChild().AddComponent(new UILabel());
        {
            totalLabel.SetText("Total: 0");
            totalLabel.SetPivot(Pivot.Left);
            totalLabel.SetFontSize(16);
            totalLabel.GetTransform().SetLocalPosition(-430, 120);
        }
        AddChild(hand = new UIGameHand());
        {
            hand.GetTransform().SetLocalPosition(-565 + UICardDisplayer.CardWidth / 2, 240);
        }
        actionIcon = CreateChild().AddComponent(new UISprite());
        {
            actionIcon.SetEnabled(false);
            actionIcon.GetTransform().SetLocalPosition(430, 240);
        }
        AddChild(phaseResulter = new UIGamePhaseResult());
        {
            phaseResulter.GetTransform().SetLocalPosition(0, 140);
            phaseResulter.SetDepth(5);
        }
        
        animator = AddComponent(new UIAnimator());
    }
    
    public UIGameHealthbar GetHealthbar() { return healthBar; }
    public UIGameItemList GetItemList() { return itemList; }
    public UIGameHand GetHand() { return hand; }
    public UIGamePhaseResult GetPhaseResulter() { return phaseResulter; }
    
    /**
     * Sets the player instance to display the information of.
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
        isAi = player instanceof GameAIPlayer;
        
        // If Ai, invert the Y position so it looks symmetrical.
        if(isAi && !didInvertUi)
        {
            didInvertUi = true;
            for(UIObject child : GetChildren())
            {
                UITransform transform = child.GetTransform();
                Vector2 pos = transform.GetLocalPosition();
                pos.Y = -pos.Y;
                transform.SetLocalPosition(pos);
            }
        }
        
        // Set name
        nameLabel.SetText(player.GetName());
        
        // Setup health bar
        healthBar.SetPlayer(player);
        
        // Setup item list
        itemList.SetPlayer(player);
        
        // Set total
        RefreshTotal();
        
        // Setup hand
        hand.SetPlayer(player);
        
        // Setup phase result displayer
        phaseResulter.SetPlayer(player);
    }
    
    /**
     * Disposes current state to be ready for a new session.
     */
    public void Dispose()
    {
        hand.Dispose();
    }

    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player) { return null; }

    public @Override Events OnNewPhaseState()
    {
        RefreshTotal();
        return null;
    }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg) { return null; }

    public @Override Events OnDrawState(GamePlayer player, Card card)
    {
        RefreshTotal();
        if(player == this.player)
            return ShowActionIcon("icon-arrow-left");
        return null;
    }

    public @Override Events OnSkipState(GamePlayer player)
    {
        if(player == this.player)
            return ShowActionIcon("icon-cross");
        return null;
    }

    public @Override Events OnItemUseState(GamePlayer player, GameItem item)
    {
        RefreshTotal();
        return null;
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        animator.RemoveAnime(ActionAniName);
    }
    
    /**
     * Refreshes total label.
     */
    private void RefreshTotal()
    {
        if(player.IsHuman() || player.IsRevealing())
            totalLabel.SetText("Total: " + player.GetHand().GetTotalCardValue());
        else
            totalLabel.SetText("Total: ?? + " + player.GetHand().GetTotalVisibleValue());
    }
    
    /**
     * Plays action icon animation and returns an event to listen to finish.
     */
    private Events ShowActionIcon(String spritename)
    {
        // Create event
        Events event = new Events();
        
        // Setup icon.
        actionIcon.SetSpritename(spritename);
        actionIcon.SetEnabled(true);
        
        // Setup animation.
        Anime actionAni = animator.CreateAnime(ActionAniName);
        UITransform transform = actionIcon.GetTransform();
        
        // Build animation frames.
        actionAni.AddSection(0, 15, (progress) -> {
            actionIcon.SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
            float scale = Easing.BackEaseOut(progress, 0.5f, 0.5f, 0.25f);
            transform.SetLocalScale(scale, scale);
        });
        actionAni.AddSection(15, 45, (progress) -> {
            actionIcon.SetAlpha(Easing.CircEaseIn(progress, 1f, -1f, 0));
        });
        
        // Set callback.
        actionAni.AddEvent(actionAni.GetDuration(), () -> {
            animator.RemoveAnime(ActionAniName);
            event.Invoke(null);
        });
        
        // Play
        actionAni.Play();
        
        // Return the event.
        return event;
    }
}
