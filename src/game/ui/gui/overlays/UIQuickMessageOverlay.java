/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.EaseType;
import game.ui.gui.components.UIAnimator;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import java.awt.Color;

/**
 * A quick message displayer.
 * @author jerrykim
 */
public class UIQuickMessageOverlay extends UIOverlay {
    
    private UILabel messageLabel;
    private Anime messageAni;
    
    
    public UIQuickMessageOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Make sure the message is displayed just below the dialog overlay.
        GetObject().SetDepth(100000000 - 1);
        
        UISprite bg = GetObject().CreateChild().AddComponent(new UISprite());
        {
            bg.SetSpritename("box");
            bg.SetColor(Color.black);
            bg.SetSize(1280, 64);
            bg.SetEnabled(false);
        }
        messageLabel = GetObject().CreateChild().AddComponent(new UILabel());
        {
            messageLabel.SetEnabled(false);
        }
        
        messageAni = animator.CreateAnime("messageani");
        {
            messageAni.AddEvent(0, () -> {
                bg.SetEnabled(true);
                messageLabel.SetEnabled(true);
            });
            messageAni.AddSection(0, 15, EaseType.EaseOut, (progress) -> {
                bg.SetAlpha(progress * 0.5f);
                messageLabel.SetAlpha(progress);
                bg.SetSize(1280, (int)(progress * 64));
            });
            messageAni.AddSection(120, 135, EaseType.EaseIn, (progress) -> {
                progress = 1 - progress;
                bg.SetAlpha(progress * 0.5f);
                messageLabel.SetAlpha(progress);
                bg.SetSize(1280, (int)(progress * 64));
            });
            messageAni.AddEvent(135, () -> {
                bg.SetEnabled(false);
                messageLabel.SetEnabled(false);
            });
        }
    }
    
    /**
     * Shows the specified message string.
     */
    public void ShowMessage(String text)
    {
        messageLabel.SetText(text);
        messageAni.PlayAt(0);
    }
}
