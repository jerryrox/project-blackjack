/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;
import game.debug.Debug;
import game.ui.gui.objects.UIObject;
import game.ui.gui.components.UIComponent;

/**
 * Basis of any displayable elements in the GUI.
 * Can't be used as-is.
 * @author jerrykim
 */
public abstract class UIDisplayer extends UIComponent {
    
    /**
     * Parent displayer in the hierarchy.
     */
    protected UIDisplayer parent = null;
    
    /**
     * Local transparency value of the displayer.
     */
    private float alpha = 1;
    
    
    protected UIDisplayer()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Search in the hierarchy for objects with a displayer.
        UIObject obj = uiObject;
        while(parent == null)
        {
            obj = obj.GetParent();
            if(obj == null)
                break;
            parent = obj.GetComponent(UIDisplayer.class);
        }
    }
    
    /**
     * Sets the local alpha value of the displayer.
     */
    public void SetAlpha(float alpha) { this.alpha = alpha; }
    
    /**
     * Returns the local alpha value of the displayer.
     */
    public float GetLocalAlpha() { return alpha; }
    
    /**
     * Returns the world alpha value of the displayer.
     */
    public float GetWorldAlpha()
    {
        if(parent == null)
            return alpha;
        return alpha * parent.GetWorldAlpha();
    }
    
    /**
     * Returns the parent displayer instance, if exists.
     */
    protected UIDisplayer GetParentDisplayer() { return parent; }
}
