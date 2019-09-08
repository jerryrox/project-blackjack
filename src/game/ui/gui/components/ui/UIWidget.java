/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.ReceivesDependency;
import game.data.Vector2;
import game.ui.Pivot;
import game.ui.gui.UIRootPanel;
import game.ui.gui.components.UITransform;
import java.awt.Color;
import java.awt.Graphics;

/**
 * Basis for specific drawable elements such as labels, sprites, textures, etc.
 * @author jerrykim
 */
public abstract class UIWidget extends UIDisplayer {
    
    /**
     * Width of the rendered widget.
     */
    private int width = 100;
    
    /**
     * Height of the rendered widget.
     */
    private int height = 100;
    
    /**
     * Drawing pivot of widget.
     */
    private Pivot pivot = Pivot.Center;
    
    /**
     * Color of the widget.
     */
    private Color color = new Color(1f, 1f, 1f);
    
    @ReceivesDependency
    protected UIRootPanel rootPanel;
    
    
    protected UIWidget()
    {
        super();
    }
    
    /**
     * Sets the draw width.
     */
    public void SetWidth(int width) { this.width = width; }
    
    /**
     * Sets the draw height.
     */
    public void SetHeight(int height) { this.height = height; }
    
    /**
     * Sets the draw width & height.
     */
    public void SetSize(int width, int height) { SetWidth(width); SetHeight(height); }
    
    /**
     * Sets the draw width & height.
     */
    public void SetSize(Vector2 size) { SetWidth((int)size.X); SetHeight((int)size.Y); }
    
    /**
     * Resets the size to its natural value.
     */
    public abstract void ResetSize();
    
    /**
     * Returns the draw width.
     */
    public int GetWidth() { return width; }
    
    /**
     * Returns the draw height.
     */
    public int GetHeight() { return height; }
    
    /**
     * Returns the draw width & height.
     */
    public Vector2 GetSize() { return new Vector2(width, height); }
    
    /**
     * Sets the pivot point for drawing the widget.
     */
    public void SetPivot(Pivot pivot) { this.pivot = pivot; }
    
    /**
     * Returns the current pivot point used for drawing.
     */
    public Pivot GetPivot() { return pivot; }
    
    /**
     * Sets the color of the widget.
     */
    public void SetColor(Color color) { this.color = color; }
    
    /**
     * Returns the drawing color of widget.
     */
    public Color GetColor() { return color; }
    
    public @Override void Render(Graphics buffer)
    {
        super.Render(buffer);
        
        // Precalculate actual draw position and size.
        UITransform transform = GetTransform();
        Vector2 drawPos = transform.GetWorldPosition();
        Vector2 scale = transform.GetWorldScale();
        Vector2 actualSize = new Vector2(width * scale.X, height * scale.Y);
        switch(pivot)
        {
        case TopLeft: break;
        case Top:
            drawPos.X -= actualSize.X / 2;
            break;
        case TopRight:
            drawPos.X -= actualSize.X;
            break;
        case Left:
            drawPos.Y -= actualSize.Y / 2;
            break;
        case Center:
            drawPos.X -= actualSize.X / 2;
            drawPos.Y -= actualSize.Y / 2;
            break;
        case Right:
            drawPos.X -= actualSize.X;
            drawPos.Y -= actualSize.Y / 2;
            break;
        case BottomLeft:
            drawPos.Y -= actualSize.Y;
            break;
        case Bottom:
            drawPos.X -= actualSize.X / 2;
            drawPos.Y -= actualSize.Y;
            break;
        case BottomRight:
            drawPos.X -= actualSize.X;
            drawPos.Y -= actualSize.Y;
            break;
        }
        
        // Let the derived classes handle their own drawing stuffs.
        Draw(buffer, drawPos, scale, actualSize);
    }
    
    /**
     * Performs drawing of whatever element it's specialized in drawing.
     */
    protected abstract void Draw(Graphics buffer, Vector2 drawPos, Vector2 scale, Vector2 actualSize);
}
