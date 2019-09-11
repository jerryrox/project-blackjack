/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.debug.Debug;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;

/**
 * Overlay which displays background image at all screens.
 * @author jerrykim
 */
public class UIWallpaperOverlay extends UIOverlay {
    
    private UISprite playground;
    
    
    public UIWallpaperOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Wallpaper should be visible behind everything else.
        uiObject.SetDepth(-10000);
        
        playground = uiObject.CreateChild().AddComponent(new UISprite());
        {
            playground.SetSpritename("playground");
            playground.ResetSize();
        }
    }
}
