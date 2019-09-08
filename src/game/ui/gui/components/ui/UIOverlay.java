/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;

/**
 * Type of view which acts like an addon view to screens.
 * @author jerrykim
 */
public class UIOverlay extends UIView {
    
    public UIOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Set a default depth.
        uiObject.SetDepth(10000);
    }
}
