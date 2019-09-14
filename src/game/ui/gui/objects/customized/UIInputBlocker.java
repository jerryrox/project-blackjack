/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.customized;

import game.allocation.InitWithDependency;
import game.ui.gui.objects.UIObject;

/**
 * Resides in the highest depth and blocks input propagation
 * @author jerrykim
 */
public class UIInputBlocker extends UIObject {
    
    
    public UIInputBlocker()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        SetDepth(200000000);
    }
    
    public @Override boolean UpdateInput() { return false; }
}
