/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.IGame;
import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import java.awt.Color;

/**
 * Overlay used for graceful quitting effect.
 * @author jerrykim
 */
public class UIQuitOverlay extends UIOverlay {
    
    
    public UIQuitOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(IGame game)
    {
        UISprite dark = uiObject.CreateChild().AddComponent(new UISprite());
        {
            dark.SetSpritename("box");
            dark.SetColor(Color.black);
            dark.SetSize(1280, 720);
            dark.SetAlpha(0);
        }
        
        Anime quitAni = animator.CreateAnime("quit");
        {
            quitAni.AddSection(0, 120, (progress) -> {
                dark.SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
            });
            quitAni.AddEvent(120, () -> {
                game.ForceQuit();
            });
        }
        quitAni.PlayAt(0);
    }
    
    public @Override boolean UpdateInput() { return false; }
}
