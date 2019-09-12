/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.InitWithDependency;
import game.debug.Debug;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIRootPanel;
import game.ui.gui.UIScreenController;
import game.ui.gui.overlays.UIWallpaperOverlay;
import game.ui.gui.screens.UIHomeScreen;
import game.ui.gui.screens.UIMainScreen;
import game.ui.gui.screens.UISplashScreen;
import game.ui.gui.screens.UITestScreen;

/**
 * A specialization of UIObject for serving as the root object in the 
 * gameobject hierarchy.
 * The actual entry point of gameobject creation must originte from this object.
 * @author jerrykim
 */
public class UIScene extends UIObject {
    
    public UIScene()
    {
        super("Scene");
    }
    
    @InitWithDependency
    private void Init(UIScreenController screens, UIOverlayController overlays)
    {
        // Adjust (0,0) to center of screen.
        GetTransform().SetLocalPosition(UIRootPanel.Width / 2, UIRootPanel.Height / 2);
        
        // Show splash screen.
        //screens.ShowView(UISplashScreen.class);
        
        // Skipping splash screen for test purposes.
        screens.ShowView(UIMainScreen.class);
        overlays.ShowView(UIWallpaperOverlay.class);
        
//        screens.ShowView(UITestScreen.class);
    }
}
