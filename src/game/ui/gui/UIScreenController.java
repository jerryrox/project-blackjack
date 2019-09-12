/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.debug.Debug;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.UIObject;

/**
 * ViewController implementation for screens.
 * @author jerrykim
 */
public class UIScreenController extends UIViewController<UIScreen> {

    public UIScreenController(UIObject root)
    {
        super(root);
    }
    
    protected @Override void OnPostShow(UIScreen view)
    {
        // Hide all other views.
        views.values().forEach(v -> {
            if(v.IsShowing() && view != v)
                HideView(v);
        });
    }

    protected @Override void OnPostHide(UIScreen view) {}
}
