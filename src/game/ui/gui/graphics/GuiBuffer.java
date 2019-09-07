/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.graphics;

import game.data.Vector2;
import game.debug.Debug;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Wrapper over Graphics object to limit the usage for specific purposes.
 * @author jerrykim
 */
public class GuiBuffer implements IGuiBuffer {
    
    private Graphics graphics;
    
    private ImageObserver observer;
    
    private Font font;
    
    
    public GuiBuffer(ImageObserver observer)
    {
        this.observer = observer;
    }
    
    /**
     * Sets the graphics instance to be used during current frame.
     */
    public void SetGraphics(Graphics graphics) { this.graphics = graphics; }
    
    public @Override void RenderText(BufferedImage image, Vector2 pos, Vector2 size)
    {
        if(graphics == null)
            return;
        
        graphics.drawImage(image, (int)pos.X, (int)pos.Y, (int)size.X, (int)size.Y, observer);//(int)pos.X, (int)pos.Y, (int)size.X, (int)size.Y, observer);
    }
    
    public @Override FontMetrics GetFontMetrics(Font font)
    {
        this.font = font;
        return graphics == null ? null : graphics.getFontMetrics(font);
    }
}
