/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.objects.UIObject;

/**
 * ViewController implementation for overlays.
 * @author jerrykim
 */
public class UIOverlayController extends UIViewController<UIOverlay> {
    
    public UIOverlayController(UIObject root)
    {
        super(root);
    }
    
    protected @Override void OnPostShow(UIOverlay view) {}

    protected @Override void OnPostHide(UIOverlay view) {}
}
