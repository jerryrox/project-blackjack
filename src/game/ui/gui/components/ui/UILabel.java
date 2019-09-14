/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Vector2;
import game.debug.Debug;
import game.ui.gui.graphics.GuiFontProvider;
import java.awt.AlphaComposite;
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
     * Cached array of text splitted using new line char.
     */
    private String[] splittedText = null;
    
    /**
     * Number of lines in the text.
     */
    private int lineCount = 1;
    
    /**
     * Cached size of the actual text to be drawn.
     */
    private Vector2 cachedSize = new Vector2();
    
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
    private int prevColor = 0;
    
    /**
     * Anti alias support for text.
     */
    private RenderingHints hints = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    
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
        
        super.SetWidth(1);
        super.SetHeight(font.getSize());
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
     * Sets font style and size used by this label.
     */
    public void SetFont(int style, int size)
    {
        Font newFont = fontProvider.Get(font.getFontName(), style, size);
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
        
        splittedText = this.text.split("\n");
        lineCount = splittedText.length;
    }
    
    /**
     * Returns the text value being displayed.
     */
    public String GetText() { return text; }
    
    public @Override void SetWidth(int width) {}
    
    public @Override void SetHeight(int height)  {}
    
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
        // Handling potentially empty text cases.
        if(text == null ||text.length() == 0)
        {
            image = null;
            textChanged = false;
            return;
        }
        
        int w = (int)cachedSize.X;
        int h = (int)cachedSize.Y;
        int totalH = h * lineCount;
        
        if(textChanged)
        {
            // Add additional width if italic style has been applied.
            int italicOffset = ((font.getStyle() & Font.ITALIC) != 0) ? font.getSize() / 4 : 0;
            
            // Calculate new size
            w = 0;
            h = metrics.getHeight();
            for(String line : splittedText)
                w = Math.max(metrics.stringWidth(line) + italicOffset, w);
            totalH = h * lineCount;
            
            // Cache new size
            cachedSize.X = w;
            cachedSize.Y = h;
            
            // Create image.
            image = new BufferedImage(w, totalH, BufferedImage.TYPE_INT_ARGB);
            
            super.SetWidth(w);
            super.SetHeight(totalH);
        }
        
        // Prepare text color.
        Color color = GetColor();
        Color col = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(GetWorldAlpha() * 255f));
        
        // If no change in color or text, just return.
        if(!textChanged && prevColor == col.getRGB())
            return;
        prevColor = col.getRGB();
        
        // Set properties.
        Graphics2D g = image.createGraphics();
        
        g.setComposite(AlphaComposite.Clear);
        g.fillRect(0, 0, w, totalH);
        g.setComposite(AlphaComposite.SrcOver);
        
        g.setRenderingHints(hints);
        g.setColor(col);
        g.setFont(font);
        int y = h - h/4;
        for (String line : splittedText)
        {
            int x = 0;
            int lineWidth = metrics.stringWidth(line);
            switch(GetPivot())
            {
            case Top:
            case Center:
            case Bottom:
                x = w / 2 - lineWidth / 2;
                break;
            case TopRight:
            case Right:
            case BottomRight:
                x = w - lineWidth;
                break;
            }
            
            g.drawString(line, x, y);
            y += h;
        }
        //g.drawString(text, 0, h - h/4);
        g.dispose();
        
        textChanged = false;
    }
}
