/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.objects.components.ui;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Vector2;
import game.debug.Debug;
import game.ui.gui.UIRootPanel;
import game.ui.gui.graphics.GuiFontProvider;
import game.ui.gui.graphics.IGuiBuffer;
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
     * Whether text image should be rebuilt.
     */
    private boolean isDirty = true;
    
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
     * Sets the text value to display.
     */
    public void SetText(String text)
    {
        this.text = (text == null ? "" : text);
        isDirty = true;
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
    
    public @Override void SetLocalAlpha(float alpha)
    {
        super.SetLocalAlpha(alpha);
        isDirty = true;
    }
    
    public @Override void SetColor(Color color)
    {
        super.SetColor(color);
        isDirty = true;
    }
    
    public @Override void Render(IGuiBuffer buffer)
    {
        // Rebuild text image.
        if(isDirty)
        {
            isDirty = false;
            RebuildTextImage(buffer.GetFontMetrics(font));
        }
        
        super.Render(buffer);
    }
    
    protected @Override void Draw(IGuiBuffer buffer, Vector2 drawPos, Vector2 scale, Vector2 actualSize)
    {
        buffer.RenderText(image, drawPos, actualSize);
    }
    
    /**
     * Rebuilds text image.
     */
    private void RebuildTextImage(FontMetrics metrics)
    {
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();
            
        if(textChanged)
        {
            textChanged = false;
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
        
        // Setup anti-aliased text.
        RenderingHints hint = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Set properties.
        Graphics2D g = image.createGraphics();
        g.setRenderingHints(hint);
        g.setColor(col);
        g.setFont(font);
        g.drawString(text, 0, h - h/4);
        g.dispose();
    }
    
    /**
     * Types of methods used for drawing the label text.
     */
    public enum WrapModes {
        ResizeFreely,
        Constrained
    }
}