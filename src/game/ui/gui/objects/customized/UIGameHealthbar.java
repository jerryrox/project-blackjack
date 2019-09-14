/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Events;
import game.graphics.ColorPreset;
import game.rulesets.Card;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.PhaseResults;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.Pivot;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;
import java.awt.Color;
import java.awt.Font;

/**
 * Health bar displayer for game player.
 * @author jerrykim
 */
public class UIGameHealthbar extends UIObject implements IGameStateListener {
    
    private final String HealthChangeAniName = "healthchange";
    
    private UISprite fg;
    private UILabel healthLabel;
    
    private GamePlayer player;
    
    private UIAnimator animator;
    
    
    public UIGameHealthbar()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-2);
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(640, 30);
            bg.SetPivot(Pivot.Left);
            bg.SetColor(Color.black);
            bg.SetAlpha(0.5f);
        }
        fg = CreateChild().AddComponent(new UISprite());
        {
            fg.GetObject().SetDepth(-1);
            fg.SetSpritename("round-box");
            fg.SetWrapMode(UISprite.WrapModes.Sliced);
            fg.SetSize(640, 30);
            fg.SetPivot(Pivot.Left);
            fg.SetColor(colors.Health);
        }
        UILabel title = CreateChild().AddComponent(new UILabel());
        {
            title.SetText("HP");
            title.SetFont(Font.BOLD | Font.ITALIC, 24);
            title.SetPivot(Pivot.Left);
            title.GetTransform().SetLocalPosition(12, 0);
        }
        healthLabel = CreateChild().AddComponent(new UILabel());
        {
            healthLabel.SetText("0 / 0");
            healthLabel.SetFontSize(16);
            healthLabel.GetTransform().SetLocalPosition(320, 0);
        }
        
        animator = AddComponent(new UIAnimator());
    }
    
    /**
     * Sets the player to display health information for.
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
        Refresh(false);
    }
    
    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player) { return null; }

    public @Override Events OnNewPhaseState() { return null; }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg)
    {
        return Refresh(true);
    }

    public @Override Events OnDrawState(GamePlayer player, Card card) { return null; }

    public @Override Events OnSkipState(GamePlayer player) { return null; }
    
    public @Override Events OnItemUseState(GamePlayer player, GameItem item)
    {
        if(player == this.player)
            return Refresh(true);
        return null;
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        animator.RemoveAnime(HealthChangeAniName);
    }
    
    /**
     * Refreshes health display, optionally animating if specified.
     * May return an Events object that can be notified on animation end.
     */
    private Events Refresh(boolean animate)
    {
        int curHealth = player.GetCurHealth();
        int maxHealth = player.GetMaxHealth();
        
        // Set label immediately.
        healthLabel.SetText(String.format("%d / %d", curHealth, maxHealth));
        
        // Calculate target width;
        float targetWidth = maxHealth == 0 ? 0 : curHealth * 640f / maxHealth;
        
        // If not animate, set width instantly.
        if(!animate)
        {
            fg.SetWidth((int)targetWidth);
            return null;
        }
        
        // Animate health change.
        float curWidth = fg.GetWidth();
        if(targetWidth == curWidth)
            return null;

        Events event = new Events();
        // Build animation
        Anime anime = animator.CreateAnime(HealthChangeAniName);
        anime.AddSection(0, 25, (progress) -> {
            fg.SetWidth((int)Easing.QuadEaseOut(progress, curWidth, targetWidth - curWidth, 0));
        });
        // Setup end event
        anime.AddEvent(60, () -> {
            animator.RemoveAnime(HealthChangeAniName);
            event.Invoke(null);
        });
        // Play animation
        anime.Play();
        
        return event;
    }
}
