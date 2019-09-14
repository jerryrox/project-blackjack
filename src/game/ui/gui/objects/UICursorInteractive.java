/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects;

import game.allocation.ReceivesDependency;
import game.data.Events;
import game.data.Vector2;
import game.debug.Debug;
import game.ui.gui.UIInput;
import game.ui.gui.components.ui.UIWidget;

/**
 * UIObject implementation with cursor interactivity.
 * @author jerrykim
 */
public class UICursorInteractive extends UIObject {
    
    /**
     * Event to be fired when the interactive area has been clicked on.
     */
    public final Events Clicked = new Events();
    
    protected boolean isOver = false;
    protected boolean isOut = false;
    protected boolean isPress = false;
    protected boolean isRelease = false;
    
    protected UIWidget targetWidget;
    
    
    /**
     * Amount of offset from this object's position.
     */
    protected Vector2 offset = new Vector2();
    
    /**
     * Size of the interaction area.
     */
    protected Vector2 size = new Vector2();
    
    @ReceivesDependency
    protected UIInput input;
    
    
    public UICursorInteractive()
    {
        super();
    }
    
    /**
     * Sets the target widget to automatically adjust the interactive area with.
     */
    public void SetTarget(UIWidget widget)
    {
        targetWidget = widget;
    }
    
    public void SetOffset(Vector2 offset) { SetOffset(offset.X, offset.Y); }
    public void SetOffset(float x, float y)
    {
        offset.X = x;
        offset.Y = y;
    }
    
    public void SetSize(Vector2 size) { SetSize(size.X, size.Y); }
    public void SetSize(float x, float y)
    {
        size.X = x;
        size.Y = y;
    }
    
    public void OnCursorOver() {}
    
    public void OnCursorOut() {}
    
    public void OnCursorPress() {}
    
    public void OnCursorRelease() {}
    
    public void OnCursorClick()
    {
        Clicked.Invoke(this);
    }
    
    public @Override void Update(float deltaTime)
    {
        super.Update(deltaTime);
        
        // Automatically adjust cursor area.
        if(targetWidget != null)
        {
            offset = targetWidget.GetTransform().GetLocalPosition();
            size = GetTransform().GetWorldScale();
            size.X *= targetWidget.GetWidth();
            size.Y *= targetWidget.GetHeight();
                    
            switch(targetWidget.GetPivot())
            {
            case TopLeft:
                break;
            case Top:
                offset.X -= size.X / 2;
                break;
            case TopRight:
                offset.X -= size.X;
                break;
            case Left:
                offset.Y -= size.Y / 2;
                break;
            case Center:
                offset.X -= size.X / 2;
                offset.Y -= size.Y / 2;
                break;
            case Right:
                offset.X -= size.X;
                offset.Y -= size.Y / 2;
                break;
            case BottomLeft:
                offset.Y -= size.Y;
                break;
            case Bottom:
                offset.X -= size.X / 2;
                offset.Y -= size.Y;
                break;
            case BottomRight:
                offset.X -= size.X;
                offset.Y -= size.Y;
                break;
            }
        }
    }
    
    public @Override boolean UpdateInput()
    {
        Vector2 mousePos = input.GetMousePosition();
        boolean isClicked = false;
        
        if(!isPress)
        {
            if(IsCursorOver(mousePos))
            {
                if(!isOver)
                {
                    isOver = true;
                    OnCursorOver();
                }
                if(input.IsMouseDown())
                {
                    isPress = true;
                    OnCursorPress();
                }
            }
            else
            {
                if(isOver)
                {
                    isOver = false;
                    OnCursorOut();
                }
                
            }
        }
        if(isPress)
        {
            if(input.IsMouseUp())
            {
                isPress = false;
                OnCursorRelease();
                
                if(!IsCursorOver(mousePos))
                {
                    isOver = false;
                    OnCursorOut();
                }
                else
                {
                    OnCursorClick();
                    isClicked = true;
                }
            }
            else if(isOver && !IsCursorOver(mousePos))
            {
                isPress = false;
                OnCursorRelease();
                isOver = false;
                OnCursorOut();
            }
        }
        return !isClicked && super.UpdateInput();
    }
    
    /**
     * Whether the cursor is over the interaction area.
     */
    protected boolean IsCursorOver(Vector2 position)
    {
        Vector2 myPos = GetTransform().GetWorldPosition();
        float areaX = offset.X + myPos.X;
        float areaY = offset.Y + myPos.Y;
                
        return position.X > areaX &&
                position.Y > areaY &&
                position.X < areaX + size.X &&
                position.Y < areaY + size.Y;
    }
}
