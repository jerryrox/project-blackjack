/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.animations.Easing;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UIButton;

/**
 * Action button for game info panel.
 * @author jerrykim
 */
public class UIGameActionButton extends UIButton {
    
    private UISprite bg;
    private UISprite icon;
    private UILabel label;
    
    
    public UIGameActionButton()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        bg = CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            
            SetTarget(bg);
        }
        icon = CreateChild().AddComponent(new UISprite());
        {
            icon.GetTransform().SetLocalScale(0.4f, 0.4f);
            icon.GetTransform().SetLocalPosition(0, -15);
        }
        label = CreateChild().AddComponent(new UILabel());
        {
            label.GetTransform().SetLocalPosition(0, 25);
        }
        
        cursorOverAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1, 0.1f, 0);
            bg.GetTransform().SetLocalScale(scale, scale);
        });
        cursorOutAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.1f, -0.1f, 0);
            bg.GetTransform().SetLocalScale(scale, scale);
        });
        cursorPressAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1, 0.1f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
        cursorReleaseAni.AddSection(0, 9, (progress) -> {
            float scale = Easing.QuadEaseOut(progress, 1.1f, -0.1f, 0);
            GetTransform().SetLocalScale(scale, scale);
        });
    }
    
    /**
     * Sets displayed information on this button.
     */
    public void Setup(String bg, String icon, String label)
    {
        this.bg.SetSpritename(bg);
        this.bg.ResetSize();
        
        this.icon.SetSpritename(icon);
        this.icon.ResetSize();
        
        this.label.SetText(label);
    }
}
