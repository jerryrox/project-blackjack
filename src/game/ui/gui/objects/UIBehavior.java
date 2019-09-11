/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

/**
 * An abstract representation of "things" that can behave in UI space.
 * @author jerrykim
 */
public abstract class UIBehavior {
    
    /**
     * Whether the behavior is currently enabled.
     */
    protected boolean isEnabled = true;
    
    /**
     * Whether the behavior should be destroyed.
     */
    protected boolean isDestroying = false;
    
    
    protected UIBehavior()
    {
    }
    
    /**
     * Sets the enabled state of the behavior.
     * @param isEnabled 
     */
    public void SetEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
        if(isEnabled)
            OnEnable();
        else
            OnDisable();
    }
    
    /**
     * Returns whether the behavior is currently enabled.
     * However, this doesn't necessarily mean the behavior is eligible for update.
     * For example, a component may not update even though isEnabled is true, but
     * its gameobject instance is inactive in hierarchy.
     */
    public boolean IsEnabled() { return isEnabled; }
    
    /**
     * Flags this behavior for destruction. The implementing class must specify how
     * the behavior will be destroyed.
     * For example, destroyed gameobject will be removed from the object hierarchy.
     */
    public void Destroy() { isDestroying = true; }
    
    /**
     * Returns whether the object is flagged for destruction.
     */
    public boolean IsDestroying() { return isDestroying; }
    
    /**
     * Updates the behavior with specified delta time.
     * @param deltaTime 
     */
    public abstract void Update(float deltaTime);
    
    /**
     * Updates logics for receiving input.
     * Returns whether input will further propagate to lower-depth objects.
     */
    public boolean UpdateInput() { return true; }
    
    /**
     * Event called when the behavior is about to be destroyed.
     */
    public abstract void OnDestroy();
    
    /**
     * Event called when the implementing object, or the component's object has become active.
     */
    protected abstract void OnEnable();
    
    /**
     * Event called when the implementing object, or the component's object has become inactive.
     */
    protected abstract void OnDisable();
}
