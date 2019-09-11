/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Vector2;
import game.debug.Debug;
import game.ui.gui.graphics.GuiFontProvider;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Draws text elements on the screen.
 * 
 * @author jerrykim
 */
public class UILabel extends UIWidget {
    
    /**
     * The text value to draw on screen.
     */
    private String text = "";
    
    /**
     * Current wrap mode of the text.
     */
    private WrapModes wrapMode = WrapModes.ResizeFreely;
    
    /**
     * Whether the text has changed when label was dirty.
     */
    private boolean textChanged = true;
    
    /**
     * Current font in use.
     */
    private Font font;
    
    /**
     * Text rendered as image.
     */
    private BufferedImage image;
    
    /**
     * Holds array of previous color values for performance issues.
     */
    private float[] prevColor = new float[] { -1, -1, -1, -1 };
    
    @ReceivesDependency
    private GuiFontProvider fontProvider;
    
    
    public UILabel()
    {
        super();
    }
    
    @InitWithDependency
    private void Init()
    {
        // Set default font.
        font = fontProvider.Get(18);
    }
    
    /**
     * Sets font used by this label.
     */
    public void SetFont(String font, int style, int size)
    {
        Font newFont = fontProvider.Get(font, style, size);
        if(newFont == null)
            return;
        this.font = newFont;
        textChanged = true;
    }
    
    /**
     * Sets the font size of currently using font.
     */
    public void SetFontSize(int size)
    {
        Font newFont = fontProvider.Get(font.getFontName(), font.getStyle(), size);
        if(newFont == null)
            return;
        this.font = newFont;
        textChanged = true;
    }
    
    /**
     * Sets the style of currently using font.
     */
    public void SetFontStyle(int style)
    {
        Font newFont = fontProvider.Get(font.getFontName(), style, font.getSize());
        if(newFont == null)
            return;
        this.font = newFont;
        textChanged = true;
    }
    
    /**
     * Sets the text value to display.
     */
    public void SetText(String text)
    {
        this.text = (text == null ? "" : text);
        textChanged = true;
    }
    
    /**
     * Returns the text value being displayed.
     */
    public String GetText() { return text; }
    
    public @Override void SetWidth(int width)
    {
        if(wrapMode == WrapModes.Constrained)
            super.SetWidth(width);
    }
    
    public @Override void SetHeight(int height) 
    {
        if(wrapMode == WrapModes.Constrained)
            super.SetHeight(height);
    }
    
    public @Override void ResetSize() { SetSize(image.getWidth(), image.getHeight()); }
    
    public @Override void Render(Graphics buffer)
    {
        // Rebuild text image.
        RebuildTextImage(buffer.getFontMetrics(font));
        
        super.Render(buffer);
    }
    
    protected @Override void Draw(Graphics buffer, Vector2 drawPos, Vector2 scale, Vector2 actualSize)
    {
        buffer.drawImage(image, (int)drawPos.X, (int)drawPos.Y, (int)actualSize.X, (int)actualSize.Y, rootPanel);
    }
    
    /**
     * Rebuilds text image.
     */
    private void RebuildTextImage(FontMetrics metrics)
    {
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();
        
        // Handling potentially empty text cases.
        if(w <= 0 || h <= 0)
        {
            image = null;
            textChanged = false;
            return;
        }
            
        if(textChanged)
        {
            // Create image.
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            
            if(wrapMode == WrapModes.ResizeFreely)
            {
                super.SetWidth(w);
                super.SetHeight(h);
            }
        }
        
        // Prepare text color.
        Color color = GetColor();
        Color col = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(GetWorldAlpha() * 255f));
        
        // If no change in color or text, just return.
        if(!textChanged && prevColor[0] == col.getAlpha() && prevColor[1] == col.getRed() && prevColor[2] == col.getGreen() && prevColor[3] == col.getBlue())
            return;
        prevColor[0] = col.getAlpha();
        prevColor[1] = col.getRed();
        prevColor[2] = col.getGreen();
        prevColor[3] = col.getBlue();
        
        // Setup anti-aliased text.
        RenderingHints hint = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Set properties.
        Graphics2D g = image.createGraphics();
        g.setRenderingHints(hint);
        g.setColor(col);
        g.setFont(font);
        g.drawString(text, 0, h - h/4);
        g.dispose();
        
        textChanged = false;
    }
    
    /**
     * Types of methods used for drawing the label text.
     */
    public enum WrapModes {
        ResizeFreely,
        Constrained
    }
}
