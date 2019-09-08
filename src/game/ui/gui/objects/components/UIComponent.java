/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.components;

import game.ui.gui.objects.UIBehavior;
import game.ui.gui.objects.UIObject;
import java.awt.Graphics;

/**
 * Represents an abstract component to be attached to gameobjects.
 * @author jerrykim
 */
public abstract class UIComponent extends UIBehavior {
    
    /**
     * The UIObject instance which this component is attached to.
     */
    protected UIObject uiObject;

    
    protected UIComponent()
    {
        super();
    }
    
    /**
     * Returns the object which the component is attached to.
     */
    public UIObject GetObject() { return uiObject; }
    
    /**
     * Convenience function for accessing the owner object's transform.
     */
    public UITransform GetTransform() { return uiObject.GetTransform(); }
    
    /**
     * Assigns the UIObject instance to which the component is attached.
     */
    public void AssignObject(UIObject obj)
    {
        if(uiObject == null)
            uiObject = obj;
    }
    
    /**
     * Handles rendering routine of the component.
     * Does nothing by default.
     */
    public void Render(Graphics buffer) {}

    public @Override void Update(float deltaTime) {}
    
    public @Override void OnDestroy() {}

    protected @Override void OnEnable() {}

    protected @Override void OnDisable() {}
}
