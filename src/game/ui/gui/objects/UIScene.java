/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.InitWithDependency;
import game.ui.Pivot;
import game.ui.gui.UIRootPanel;
import game.ui.gui.objects.components.ui.UILabel;

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
    private void Init()
    {
        // Adjust (0,0) to center of screen.
        GetTransform().SetLocalPosition(UIRootPanel.Width / 2, UIRootPanel.Height / 2);
        
        
    }
}
