/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.debug.Debug;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;

/**
 * Home screen.
 * @author jerrykim
 */
public class UIHomeScreen extends UIScreen {
    
    private Anime onShownAni;
    
    
    public UIHomeScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        Debug.Log("Init");
        UISprite title = uiObject.CreateChild().AddComponent(new UISprite());
        {
            title.SetSpritename("title");
            title.ResetSize();
        }
        
        onShownAni = animator.CreateAnime("onshown");
        {
            onShownAni.AddEvent(0, () -> {
                title.SetAlpha(0);
            });
            onShownAni.AddSection(12, 48, (progress) -> {
                title.SetAlpha(Easing.QuadEaseOut(progress, 0, 1f, 0));
                
                float scale = Easing.BackEaseOut(progress, 0.5f, 0.5f, 0.6f);
                title.GetTransform().SetLocalScale(scale, scale);
            });
        }
    }
    
    public @Override void OnActive()
    {
        super.OnActive();
        
        onShownAni.PlayAt(0);
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        onShownAni.Stop();
    }
}
