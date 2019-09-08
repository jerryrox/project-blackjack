/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.graphics;

import game.data.Rect;
import javax.swing.ImageIcon;

/**
 * Information of a single sprite contained in atlas.
 * @author jerrykim
 */
public class UISpriteInfo {
    
    private ImageIcon image;
    private Rect sliceRect;
    
    
    public UISpriteInfo(ImageIcon image, Rect sliceRect)
    {
        this.image = image;
        this.sliceRect = sliceRect;
    }
    
    /**
     * Returns the image data of the sprite info.
     */
    public ImageIcon GetImage() { return image; }
    
    /**
     * Returns the slice rect value of the sprite info.
     * This does not clone the original reference and therefore, you should never
     * modify any internal value!
     */
    public Rect GetSliceRect() { return sliceRect; }
}
