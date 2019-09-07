/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.graphics;

import game.data.Vector2;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;

/**
 * Public interface of GuiBuffer visible to general objects.
 * @author jerrykim
 */
public interface IGuiBuffer {
    
    /**
     * Renders text using specified values.
     */
    void RenderText(BufferedImage image, Vector2 pos, Vector2 size);
    
    /**
     * Returns the font metrics instance for specified font.
     */
    FontMetrics GetFontMetrics(Font font);
}
