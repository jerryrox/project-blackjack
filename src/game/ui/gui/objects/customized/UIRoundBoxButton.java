/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.EaseType;
import game.animations.Easing;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;
import java.awt.Color;

/**
 * UIButton implementation with round box sprite.
 * @author jerrykim
 */
public class UIRoundBoxButton extends UIButton {
    
    private UILabel label;
    private UISprite bg;
    
    private int bgWidth = 640;
    private int bgHeight = 48;
    
    
    @InitWithDependency
    private void Init()
    {
        bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("round-box");
            bg.SetWrapMode(UISprite.WrapModes.Sliced);
            bg.SetSize(640, bgHeight);
        }
        label = CreateChild().AddComponent(new UILabel());
        {
            label.SetText("Button");
        }
        
        SetTarget(bg);
        
        cursorOverAni.AddSection(0, 6, EaseType.QuadEaseOut, (progress) -> {
            bg.SetSize((int)Easing.Linear(progress, bgWidth, bgWidth*0.2f, 0), bgHeight);
        });
        cursorOutAni.AddSection(0, 6, EaseType.QuadEaseIn, (progress) -> {
            bg.SetSize((int)Easing.Linear(progress, bgWidth*1.2f, -bgWidth*0.2f, 0), bgHeight);
        });
        cursorPressAni.AddSection(0, 6, EaseType.BackEaseOut, (progress) -> {
            float scale = Easing.Linear(progress, 1, 0.1f, 0.1f);
            GetTransform().SetLocalScale(scale, scale);
        });
        cursorReleaseAni.AddSection(0, 6, EaseType.QuadEaseIn, (progress) -> {
            float scale = Easing.Linear(progress, 1.1f, -0.1f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
    }
    
    /**
     * Sets the width of the background sprite.
     * @param width 
     */
    public void SetWidth(int width)
    {
        bgWidth = width;
        bg.SetSize(bgWidth, bgHeight);
    }
    
    public void SetBgColor(Color color) { bg.SetColor(color); }
    
    public void SetTextColor(Color color) { label.SetColor(color); }
    
    public void SetLabel(String text) { label.SetText(text); }
}