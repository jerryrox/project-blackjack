/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.data.Events;
import game.debug.Debug;
import game.graphics.ColorPreset;
import game.rulesets.Card;
import game.rulesets.GameItem;
import game.rulesets.GamePlayer;
import game.rulesets.GameProcessor;
import game.rulesets.PhaseResults;
import game.rulesets.ui.gui.IGameStateListener;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UIDisplayer;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIObject;
import java.awt.Color;
import java.awt.Font;

/**
 * Phase evaluation result displayer.
 * @author jerrykim
 */
public class UIGamePhaseResult extends UIObject implements IGameStateListener {
    
    private UIDisplayer totalPanel;
    private UILabel totalLabel;
    private UISprite totalBg;
    
    private UIDisplayer damagePanel;
    private UILabel damageLabel;
    private UISprite damageBg;
    
    private Anime totalAni;
    private Anime damageAni;
    
    private Events totalAniEvent;
    private Events damageAniEvent;
    
    private GamePlayer player;
    private GameProcessor gameProcessor;
    
    
    public UIGamePhaseResult()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        totalPanel = CreateChild().AddComponent(new UIDisplayer());
        {
            UIObject totalObj = totalPanel.GetObject();
            totalLabel = totalObj.CreateChild().AddComponent(new UILabel());
            {
                totalLabel.SetFontSize(36);
                totalLabel.SetFontStyle(Font.BOLD);
            }
            totalBg = totalObj.CreateChild().AddComponent(new UISprite());
            {
                totalBg.GetObject().SetDepth(-1);
                totalBg.SetSpritename("glow");
                totalBg.SetColor(Color.black);
                totalBg.SetAlpha(1f);
                totalBg.SetSize(240, 64);
            }
            totalPanel.GetObject().SetActive(false);
        }
        damagePanel = CreateChild().AddComponent(new UIDisplayer());
        {
            UIObject damageObj = damagePanel.GetObject();
            damageLabel = damageObj.CreateChild().AddComponent(new UILabel());
            {
                damageLabel.SetFontSize(36);
                damageLabel.SetColor(colors.Dark);
            }
            damageBg = damageObj.CreateChild().AddComponent(new UISprite());
            {
                damageBg.GetObject().SetDepth(-1);
                damageBg.GetTransform().SetLocalPosition(-8, 0);
                damageBg.SetSpritename("icon-damage");
                damageBg.ResetSize();
            }
            damagePanel.GetObject().SetActive(false);
        }
        
        UIAnimator animator = AddComponent(new UIAnimator());
        totalAni = animator.CreateAnime("total");
        {
            totalAni.AddEvent(0, () -> {
                totalPanel.GetObject().SetActive(true);
            });
            totalAni.AddSection(0, 20, (progress) -> {
                totalPanel.SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
                float scale = Easing.QuadEaseOut(progress, 0.25f, 0.75f, 0);
                totalPanel.GetTransform().SetLocalScale(scale, scale);
            });
            totalAni.AddSection(60, 75, (progress) -> {
                totalPanel.SetAlpha(Easing.QuadEaseIn(progress, 1, -1, 0));
            });
            totalAni.AddEvent(60, () -> {
                totalAniEvent.Invoke(null);
            });
            totalAni.AddEvent(75, () -> totalPanel.GetObject().SetActive(false));
        }
        damageAni = animator.CreateAnime("damage");
        {
            damageAni.AddEvent(0, () -> {
                damagePanel.GetObject().SetActive(true);
            });
            damageAni.AddSection(0, 30, (progress) -> {
                damagePanel.SetAlpha(Easing.CubicEaseOut(progress, 0, 1, 0));
                float scale = Easing.ElasticEaseOut(progress, 2, -1, 0.5f);
                damagePanel.GetTransform().SetLocalScale(scale, scale);
            });
            damageAni.AddSection(45, 60, (progress) -> {
                damagePanel.SetAlpha(Easing.QuadEaseIn(progress, 0, -1, 0));
            });
            damageAni.AddEvent(60, () -> {
                damagePanel.GetObject().SetActive(false);
                if(damageAniEvent != null)
                    damageAniEvent.Invoke(null);
            });
        }
    }

    /**
     * Sets the player this result displays for.
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
    }
    
    /**
     * Sets the game processor managing this session.
     */
    public void SetGameProcessor(GameProcessor gameProcessor)
    {
        this.gameProcessor = gameProcessor;
    }
    
    public @Override Events OnSetTurnState(GamePlayer player) { return null; }

    public @Override Events OnTurnEndState(GamePlayer player)
    {
        if(!player.IsHuman() && gameProcessor.ShouldEvalPhase())
        {
            totalLabel.SetText(String.valueOf(this.player.GetHand().GetTotalCardValue()));

            totalAniEvent = new Events();
            totalAni.PlayAt(0);
            return totalAniEvent;
        }
        return null;
    }

    public @Override Events OnNewPhaseState() { return null; }

    public @Override Events OnPhaseEndState() { return null; }

    public @Override Events OnEvaluatedState(PhaseResults result, int humanDmg, int aiDmg)
    {
        if((player.IsHuman() && aiDmg > 0) || (!player.IsHuman() && humanDmg > 0))
        {
            damageLabel.SetText(String.valueOf(player.IsHuman() ? aiDmg : humanDmg));
            
            damageAniEvent = new Events();
            damageAni.PlayAt(0);
            return damageAniEvent;
        }
        return null;
    }

    public @Override Events OnDrawState(GamePlayer player, Card card) { return null; }

    public @Override Events OnSkipState(GamePlayer player) { return null; }

    public @Override Events OnItemUseState(GamePlayer player, GameItem item) { return null; }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        totalPanel.GetObject().SetActive(false);
        damagePanel.GetObject().SetActive(false);
        totalAni.Stop();
        damageAni.Stop();
    }
}
