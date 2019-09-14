/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components;

import game.data.Vector2;
import game.ui.gui.objects.UIObject;

/**
 * Holds transformation data of an UIObject instance.
 * @author jerrykim
 */
public class UITransform extends UIComponent {
    
    /**
     * Local position of the object.
     */
    private Vector2 localPosition = new Vector2(0, 0);
    
    /**
     * World position of the object.
     */
    private Vector2 worldPosition = new Vector2(0, 0);
    
    /**
     * Local scale of the object.
     */
    private Vector2 localScale = new Vector2(1, 1);
    
    /**
     * World scale of the object.
     */
    private Vector2 worldScale = new Vector2(1, 1);
    
    /**
     * Transform component of the parent.
     */
    private UITransform parent;
    
    /**
     * Whether world transform values should be rebuilt.
     * 
     * NOT a sophisticalted design though.
     * Needs redesigning when I got time.
     */
    private boolean rebuildWorldTransform = true;
    
    
    public UITransform(UITransform parent)
    {
        super();
        
        this.parent = parent;
    }
    
    /**
     * Sets the local position of the transform.
     */
    public void SetLocalPosition(Vector2 pos) { SetLocalPosition(pos.X, pos.Y); }
   
    /**
     * Sets the local position of the transform.
     */
    public void SetLocalPosition(float x, float y)
    {
        localPosition.X = x;
        localPosition.Y = y;
        rebuildWorldTransform = true;
    }
    
    /**
     * Sets the local scale of the transform.
     */
    public void SetLocalScale(Vector2 scale) { SetLocalScale(scale.X, scale.Y); }
    
    /**
     * Sets the local scale of the transform.
     * @param x
     * @param y 
     */
    public void SetLocalScale(float x, float y)
    {
        localScale.X = x;
        localScale.Y = y;
        rebuildWorldTransform = true;
    }
    
    /**
     * Returns the local position of the transform.
     */
    public Vector2 GetLocalPosition() { return localPosition.Clone(); }
    
    /**
     * Returns the local scale of the transform.
     */
    public Vector2 GetLocalScale() { return localScale.Clone(); }
    
    /**
     * Returns the position of the transform in world space.
     */
    public Vector2 GetWorldPosition()
    {
        if(rebuildWorldTransform)
            SignalParentForWorldTransform();
        return worldPosition.Clone();
    }
    
    /**
     * Returns the scale of the transform in world space.
     */
    public Vector2 GetWorldScale()
    {
        if(rebuildWorldTransform)
            SignalParentForWorldTransform();
        return worldScale.Clone();
    }
    
    /**
     * Converts the specified world position to local position and returns a new vector holding this value.
     */
    public Vector2 InverseTransformPoint(Vector2 point)
    {
        Vector2 newPoint = point.Clone();
        newPoint.X -= worldPosition.X;
        newPoint.Y -= worldPosition.Y;
        newPoint.X /= worldScale.X;
        newPoint.Y /= worldScale.Y;
        return newPoint;
    }
    
    /**
     * Converts the specified local position to world position and returns a new vector holding this value.
     */
    public Vector2 TransformPoint(Vector2 point)
    {
        Vector2 newPoint = point.Clone();
        newPoint.X *= worldScale.X;
        newPoint.Y *= worldScale.Y;
        newPoint.X += worldPosition.X;
        newPoint.Y += worldPosition.Y;
        return newPoint;
    }
    
    public @Override void Update(float deltaTime)
    {
        if(rebuildWorldTransform)
            CalculateWorldTransform();
    }
    
    /**
     * Signals the top-most parent who needs to refresh world transform for calculation now.
     */
    private void SignalParentForWorldTransform()
    {
        UITransform target = this;
        UITransform checking = target;
        while(true)
        {
            checking = checking.parent;
            if(checking == null || checking == target)
                break;
            if(checking.rebuildWorldTransform)
                target = checking;
        }
        if(target != null)
            target.CalculateWorldTransform();
    }
    
    /**
     * Calculates world transform values of this transform.
     */
    private void CalculateWorldTransform()
    {
        rebuildWorldTransform = false;
        
        // Apply parent's transform values.
        if(parent != null)
        {
            Vector2 pwp = parent.worldPosition;
            Vector2 pws = parent.worldScale;
            worldPosition.CopyFrom(pwp);
            worldScale.CopyFrom(pws);
            
            worldPosition.X += localPosition.X * pws.X;
            worldPosition.Y += localPosition.Y * pws.Y;
            worldScale.X *= localScale.X;
            worldScale.Y *= localScale.Y;
        }
        else
        {
            worldPosition.CopyFrom(localPosition);
            worldScale.CopyFrom(localScale);
        }
        
        // Signal children as well.
        for(UIObject obj : uiObject.GetChildren())
        {
            obj.GetTransform().CalculateWorldTransform();
        }
    }
}
