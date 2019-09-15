/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.animations.Easing;
import game.data.ActionT;
import game.entities.Stat;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.io.store.UserStore;
import game.ui.Pivot;
import game.ui.gui.UIOverlayController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;
import game.ui.gui.overlays.DialogPresetter;
import java.awt.Color;
import java.awt.Font;

/**
 * Stat displayer for main screen.
 * @author jerrykim
 */
public class UIMainStatDisplayer extends UIButton {
    
    private UILabel nameLabel;
    private UILabel levelLabel;
    private UILabel valueLabel;
    private UILabel costLabel;
    
    private Stat stat;
    
    private ActionT<Integer> onLevelChange;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private UserStore userStore;
    
    @ReceivesDependency
    private UserEntity user;
    
    
    public UIMainStatDisplayer()
    {
        super();
        
        onLevelChange = (level) -> {
            RefreshDisplay();
        };
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(360, 64);
            bg.SetColor(colors.Neutral);
            bg.SetAlpha(0.25f);
        }
        nameLabel = CreateChild().AddComponent(new UILabel());
        {
            nameLabel.SetPivot(Pivot.TopLeft);
            nameLabel.SetFontStyle(Font.BOLD);
            nameLabel.GetTransform().SetLocalPosition(-160, -28);
        }
        levelLabel = CreateChild().AddComponent(new UILabel());
        {
            levelLabel.SetPivot(Pivot.TopLeft);
            levelLabel.SetAlpha(0.75f);
            levelLabel.SetFontSize(14);
            levelLabel.GetTransform().SetLocalPosition(-155, -5);
        }
        valueLabel = CreateChild().AddComponent(new UILabel());
        {
            valueLabel.SetPivot(Pivot.TopLeft);
            valueLabel.SetAlpha(0.75f);
            valueLabel.SetFontSize(13);
            valueLabel.GetTransform().SetLocalPosition(-155, 12);
        }
        costLabel = CreateChild().AddComponent(new UILabel());
        {
            costLabel.SetPivot(Pivot.TopRight);
            costLabel.SetAlpha(0.75f);
            costLabel.SetFontSize(14);
            costLabel.GetTransform().SetLocalPosition(160, -28);
        }
        
        // Adding some hover effects.
        SetTarget(bg);
        cursorOverAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseOut(progress, 0.25f, 0.25f, 0));
        });
        cursorOutAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseOut(progress, 0.5f, -0.25f, 0));
        });
        cursorPressAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1f, 0.1f, 0f);
            GetTransform().SetLocalScale(scale, scale);
        });
        cursorReleaseAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.1f, -0.1f, 0f);
            GetTransform().SetLocalScale(scale, scale);
        });
        
        // Set purchase event.
        Clicked.Add((arg) -> {
            OnStatButton();
        });
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        // Unhook from level up event.
        if(stat != null)
            stat.Level.Changed.Remove(onLevelChange);
    }
    
    /**
     * Sets display using specified params.
     */
    public void Setup(Stat stat)
    {
        this.stat = stat;
        // Hook on to level up event.
        stat.Level.Changed.Add(onLevelChange);
        // Refresh display once.
        RefreshDisplay();
    }
    
    private void RefreshDisplay()
    {
        nameLabel.SetText(stat.GetName());
        levelLabel.SetText("Level: " + stat.Level.GetValue());
        valueLabel.SetText(stat.GetDescription());
        costLabel.SetText("Upgrade cost: " + stat.GetCost());
    }
    
    private void OnStatButton()
    {
        if(stat == null)
            return;
        // If not enough gold, return.
        if(user.Gold.GetValue() < stat.GetCost())
        {
            DialogPresetter.NotEnoughGold(overlays, null);
            return;
        }
        
        // Confirm upgrade
        DialogPresetter.ConfirmUpgradeStat(overlays, stat,
            () -> {
                // Decrease gold.
                user.Gold.SetValue(user.Gold.GetValue() - stat.GetCost());
                // Increase stat
                stat.Level.SetValue(stat.Level.GetValue() + 1);
                // Save user info
                userStore.Save();
            },
            null
        );
    }
}
