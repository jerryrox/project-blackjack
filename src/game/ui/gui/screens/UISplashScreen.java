/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.overlays.UIWallpaperOverlay;
import java.awt.Color;

/**
 * Splash screen.
 * @author jerrykim
 */
public class UISplashScreen extends UIScreen {
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    
    public UISplashScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        UISprite bg = uiObject.CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("box");
            bg.SetSize(1280, 720);
            bg.SetColor(Color.black);
        }
        
        UISprite logo = uiObject.CreateChild().AddComponent(new UISprite());
        logo.SetSpritename("logo-aut");
        logo.ResetSize();
        logo.GetTransform().SetLocalScale(0.4f, 0.4f);
        logo.SetAlpha(0f);
        
        Anime logoAni = animator.CreateAnime("logo-ani");
        {
            logoAni.AddSection(10, 50, (progress) -> {
                logo.SetAlpha(Easing.QuadEaseOut(progress, 0, 1, 0));
            });
            logoAni.AddSection(110, 160, (progress) -> {
                logo.SetAlpha(Easing.QuadEaseIn(progress, 1, -1, 0));
            });
            logoAni.AddEvent(161, () -> {
                EndSplash();
            });
            logoAni.Play();
        }
    }
    
    private void EndSplash()
    {
        screens.ShowView(UIHomeScreen.class);
    }
}
