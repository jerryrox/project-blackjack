/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Yieldable;
import game.debug.Debug;
import game.debug.ILogger;
import game.ui.gui.components.UIComponent;
import game.ui.gui.components.UITransform;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * Represents a gameobject in the game.
 * A gameobject does not have any significant functionality in the user's POV,
 * but the types of components attached to the object do.
 * @author jerrykim
 */
public class UIObject extends UIBehavior {
    
    /**
     * Name of this object, useful for debugging.
     */
    private String name = null;
    
    /**
     * The depth of the object which determines the rendering and updating order in the
     * hierarchy against children in the same leaf.
     */
    private int depth = 0;
    
    /**
     * Whether the children list is dirty and needs cleanup.
     */
    private boolean isChildrenDirty = false;
    
    /**
     * Whether the gameobject is locally active.
     */
    private boolean isActive = true;
    
    /**
     * Counter for the number of parents and self that makes this object inactive in hierarchy.
     */
    private int inactiveParents = 0;
    
    /**
     * The parent object in the hierarchy.
     */
    private UIObject parent = null;
    
    /**
     * List of child objects of this object.
     */
    private ArrayList<UIObject> children = new ArrayList<>();
    
    /**
     * List of components attached to this object.
     */
    private ArrayList<UIComponent> components = new ArrayList<>();
    
    /**
     * Direct access to transform component.
     */
    private UITransform transform;
    
    @ReceivesDependency
    private IDependencyContainer dependencies;
    
    @ReceivesDependency
    private ILogger logger;

    
    public UIObject() { this(null); }
    
    public UIObject(String name)
    {
        super();
        this.name = name;
    }
    
    @InitWithDependency
    private void Init()
    {
        // Add default components.
        transform = AddComponent(new UITransform(parent == null ? null : parent.transform));
    }
    
    /**
     * Returns the name of the object.
     */
    public String GetName() { return name; }
    
    /**
     * Returns the parent object instance of this object.
     */
    public UIObject GetParent() { return parent; }
    
    /**
     * Sets the depth value of this object.
     */
    public void SetDepth(int depth)
    {
        this.depth = depth;
        // Make parent rebuild its children list.
        if(parent != null)
            parent.isChildrenDirty = true;
    }
    
    /**
     * Returns the depth of this object.
     */
    public int GetDepth() { return depth; }
    
    /**
     * Sets the active state of the object.
     */
    public void SetActive(boolean isActive)
    {
        // Handle only if change of state.
        if(this.isActive != isActive)
        {
            this.isActive = isActive;
            // If none of the parents are currently inactive, proceed with event invocation.
            if(inactiveParents <= 0)
            {
                if(isActive)
                    InvokeOnActive();
                else
                    InvokeOnInactive();
            }
            
            // Propagate active signal to all children objects.
            PropagateSetActive(true, isActive);
        }
    }
    
    /**
     * Returns whether the gameobject is locally active.
     */
    public boolean IsActive() { return isActive; }
    
    /**
     * Returns whether the gameobject is currently active relative to the object hierarchy.
     */
    public boolean IsActiveInHierarchy() { return isActive && inactiveParents <= 0; }
    
    /**
     * Adds the specified child object to the list and returns its reference.
     */
    public <T extends UIObject> T AddChild(T t)
    {
        // t mustn't be null.
        if(t == null)
        {
            logger.LogError("UIObject.AddChild - Specified object t is null!");
            return null;
        }
        // Destroying objects must be ignored.
        if(t.isDestroying)
        {
            logger.LogError("UIObject.AddChild - Attempted to add an object whose destoy flag is true!");
            return null;
        }
        
        UIObject obj = (UIObject)t;
        // If the child is already included somewhere in the hierarhy, ignore it.
        if(obj.parent != null)
        {
            logger.LogError("UIObject.AddChild - Attempted to add an object which is already placed somewhere in the hierarchy!");
            return null;
        }
        
        // Add the object as child.
        obj.parent = this;
        children.add(t);
        
        // Inject dependencies for initialization.
        dependencies.Inject(t);
        
        // Inherit active state and invoke event.
        obj.inactiveParents = this.inactiveParents + (this.isActive ? 0 : 1);
        if(obj.inactiveParents == 0)
            obj.InvokeOnActive();
        else
            obj.InvokeOnInactive();
        
        // Cleanse children list.
        isChildrenDirty = true;
        
        return t;
    }
    
    /**
     * Creates a plain UIObject instance and adds it as child.
     */
    public UIObject CreateChild() { return AddChild(new UIObject()); }
    
    /**
     * Creates a plain UIObject instance with name and adds it as child.
     */
    public UIObject CreateChild(String name) { return AddChild(new UIObject(name)); }
    
    public Iterable<UIObject> GetChildren() { return children; }
    
    /**
     * Adds specified component to the list and returns its reference.
     */
    public <T extends UIComponent> T AddComponent(T t)
    {
        // T shouldn't be null.
        if(t == null)
        {
            logger.LogError("UIObject.AddComponent - Attempted to add a null component!");
            return null;
        }
        // Destroying objects should be ignored.
        if(t.isDestroying)
        {
            logger.LogError("UIObject.AddComponent - Attempted to add a destroying component!");
            return null;
        }
        // If the component is already attached to an object, return.
        if(t.GetObject() != null)
        {
            logger.LogError("UIObject.AddComponent - The specified component is already attached to a UIObject!");
            return null;
        }
        
        // Add to components list.
        t.AssignObject(this);
        // Insert to beginning since update is performed on components in reverse-loop order
        // and UITransform must be updated first.
        components.add(0, t);
        
        // Initialize component by injection.
        dependencies.Inject(t);
        
        // Invoke active event based on this object's state.
        if(this.IsActiveInHierarchy())
            t.OnActive();
        else
            t.OnInactive();
        
        return t;
    }
    
    /**
     * Returns a component of specified type T attached to this object, if exists.
     * Due to how things work inside, it's not recommended to call this frequently.
     */
    public <T extends UIComponent> T GetComponent(Class<T> type)
    {
        for(int i=0; i<components.size(); i++)
        {
            try
            {
                return type.cast(components.get(i));
            }
            catch(Exception e) {}
        }
        return null;
    }
    
    /**
     * Returns all components of type T attached to this object, if exists.
     */
    public <T extends UIComponent> Iterable<T> GetComponents(Class<T> type)
    {
        return new Yieldable<T>(yield -> {
            for(int i=0; i<components.size(); i++)
            {
                try
                {
                    yield.Return(type.cast(components.get(i)));
                }
                catch(Exception e) {}
            }
            yield.Break();
        });
    }
    
    /**
     * Returns the transform component on this object.
     */
    public UITransform GetTransform() { return transform; }
    
    /**
     * Propagates update signal to this object, children objects, and components.
     * This method should never be called manually!
     */
    public void PropagateUpdate(float deltaTime)
    {
        // If this object is inactive, return.
        if(!isActive)
            return;
        
        // Handle update on self if enabled.
        if(isEnabled)
            Update(deltaTime);
        // Handle update on components if enabled.
        for(int i=components.size()-1; i>=0; i--)
        {
            UIComponent component = components.get(i);
            // Destroy component?
            if(component.isDestroying)
            {
                component.OnDestroy();
                components.remove(i);
                continue;
            }
            // Update component.
            if(component.isEnabled)
                component.Update(deltaTime);
        }
        
        // Handle cleansing of children list if dirty.
        if(isChildrenDirty)
        {
            isChildrenDirty = false;
            // Sort children by depth, ascending.
            children.sort((x, y) -> Integer.compare(x.depth, y.depth));
        }
        
        // Propagate update to children.
        // Loop is reversed because objects created during this process shouldn't be updated until the next frame.
        for(int i=children.size()-1; i>=0; i--)
        {
            UIObject child = children.get(i);
            // Destroying child?
            if(child.isDestroying)
            {
                child.OnDestroy();
                children.remove(i);
                continue;
            }
            // Propagate update.
            child.PropagateUpdate(deltaTime);
        }
    }
    
    /**
     * Propagates input update signal to this object, children objects, and components.
     * This method should never be called manually!
     */
    public boolean PropagateInput()
    {
        // If this object is inactive, return.
        if(!isActive)
            return true;
        
        // Send input to child with highest depth first.
        for(int i=children.size()-1; i>=0; i--)
        {
            UIObject child = children.get(i);
            // If child wishes to consume input, just return.
            if(!child.PropagateInput())
                return false;
        }
        
        // Send update signal to components.
        // When processing input for components, the return value from UpdateInput will toggle whether
        // input propagation should be stopped, instead of immediately returning.
        boolean shouldStop = false;
        for(int i=0; i<components.size(); i++)
        {
            UIComponent component = components.get(i);
            if(component.isEnabled)
                shouldStop |= !component.UpdateInput();
        }
        
        // Perform input update on the object itself.
        if(isEnabled)
            shouldStop |= !UpdateInput();
        
        // Return whether input was consumed or not.
        return !shouldStop;
    }
    
    /**
     * Propagates render signal to children objects and components.
     * This method should never be called manually!
     */
    public void PropagateRender(Graphics buffer)
    {
        // If this object is inactive, return.
        if(!isActive)
            return;
        // Let components render their stuffs.
        for(int i=0; i<components.size(); i++)
        {
            UIComponent component = components.get(i);
            if(component.isEnabled)
                component.Render(buffer);
        }
        // Propagate render signal to children.
        for(int i=0; i<children.size(); i++)
            children.get(i).PropagateRender(buffer);
    }
    
    /**
     * Event called when this object has become active in hierarchy.
     */
    public void OnActive() {}
    
    /**
     * Event called when this object has become inactive in hierarchy.
     */
    public void OnInactive() {}
    
    public @Override void Update(float deltaTime) {}

    public @Override void OnDestroy()
    {
        // Destroy all components.
        for(int i=0; i<components.size(); i++)
            components.get(i).OnDestroy();
        components.clear();
        
        // Destroy all children.
        for(int i=0; i<children.size(); i++)
            children.get(i).OnDestroy();
        children.clear();
    }
    
    /**
     * Propagates SetActive signal to the children objects.
     */
    protected void PropagateSetActive(boolean isEmitter, boolean isActive)
    {
        // Apply it locally if not the emitter.
        if(!isEmitter)
        {
            inactiveParents += isActive ? -1 : 1;
            
            // Invoke events based on implicit activation/deactivation.
            // Child active, parent active, all parents active
            if(this.isActive && isActive && inactiveParents == 0)
                InvokeOnActive();
            // Child active, parent inactive, one parent inactive)
            else if(this.isActive && !isActive && inactiveParents == 1)
                InvokeOnInactive();
        }
        // Propagate to children.
        for(int i=children.size()-1; i>=0; i--)
            children.get(i).PropagateSetActive(false, isActive);
    }
    
    protected @Override void OnEnable() {}

    protected @Override void OnDisable() {}
    
    /**
     * Invokes OnActive on this object and its components.
     */
    private void InvokeOnActive()
    {
        OnActive();
        for(int i=0; i<components.size(); i++)
        {
            UIComponent component = components.get(i);
            if(!component.isDestroying)
                components.get(i).OnActive();
        }
    }
    
    /**
     * Invokes OnInactive on this object and its components.
     */
    private void InvokeOnInactive()
    {
        OnInactive();
        for(int i=0; i<components.size(); i++)
        {
            UIComponent component = components.get(i);
            if(!component.isDestroying)
                components.get(i).OnInactive();
        }
    }
}
