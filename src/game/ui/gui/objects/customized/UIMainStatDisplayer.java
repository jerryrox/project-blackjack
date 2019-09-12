/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Easing;
import game.graphics.ColorPreset;
import game.ui.Pivot;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;
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
    
    
    public UIMainStatDisplayer()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(280, 64);
            bg.SetColor(colors.Neutral);
            bg.SetAlpha(0.25f);
        }
        nameLabel = CreateChild().AddComponent(new UILabel());
        {
            nameLabel.SetPivot(Pivot.TopLeft);
            nameLabel.SetFontStyle(Font.BOLD);
            nameLabel.GetTransform().SetLocalPosition(-120, -28);
        }
        levelLabel = CreateChild().AddComponent(new UILabel());
        {
            levelLabel.SetPivot(Pivot.TopLeft);
            levelLabel.SetAlpha(0.75f);
            levelLabel.SetFontSize(14);
            levelLabel.GetTransform().SetLocalPosition(-115, -5);
        }
        valueLabel = CreateChild().AddComponent(new UILabel());
        {
            valueLabel.SetPivot(Pivot.TopLeft);
            valueLabel.SetAlpha(0.75f);
            valueLabel.SetFontSize(13);
            valueLabel.GetTransform().SetLocalPosition(-115, 12);
        }
        
        // Adding some hover effects.
        SetTarget(bg);
        cursorOverAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseOut(progress, 0.25f, 0.25f, 0));
        });
        cursorOutAni.AddSection(0, 9, (progress) -> {
            bg.SetAlpha(Easing.QuadEaseOut(progress, 0.5f, -0.25f, 0));
        });
    }
    
    /**
     * Sets display using specified params.
     */
    public void Setup(String name, int level, String value)
    {
        nameLabel.SetText(name);
        levelLabel.SetText("Level: " + level);
        valueLabel.SetText(value);
    }
}
