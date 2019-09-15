/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.graphics;

import game.data.Rect;
import game.debug.Debug;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 * Object which holds and provides sprite images as a sprite atlas.
 * @author jerrykim
 */
public class UIAtlas {
    
    /**
     * Dictionary of all sprite information mapped to their spritename.
     */
    private HashMap<String, UISpriteInfo> atlas = new HashMap<>();
    
    /**
     * Array of all supported image extension types.
     */
    private final String[] extensions = new String[] {
        ".png", ".jpg"
    };
    
    
    public UIAtlas()
    {
    }
    
    /**
     * Returns the sprite information of specified spritename.
     */
    public UISpriteInfo GetSprite(String spritename) { return atlas.get(spritename); }
    
    /**
     * Adds the specified spritename retrieved from the file system.
     */
    public void AddSprite(String spritename) { AddSprite(spritename, new Rect()); }
    
    /**
     * Adds the specified spritename retrieved from the file system, with custom slice rect.
     * Rect specification:
     * X = Pixel distance from left
     * Y = Pixel distance from top
     * Z = Pixel distance from X
     * W = Pixel distance from Y
     * Using this, top, left, center, right, and bottom slices will be stretched.
     */
    public void AddSprite(String spritename, Rect sliceRect)
    {
        // If spritename exists, just return.
        if(atlas.containsKey(spritename))
            return;
        
        for(int i=0; i<extensions.length; i++)
        {
            // Build image path
            URL url = getClass().getResource(String.format("/assets/sprites/%s%s", spritename, extensions[i]));
            if(url == null)
                continue;
            
            // If image file does not exist, continue.
            try
            {
                // If exists, build image icon and add to atlas.
                ImageIcon image = new ImageIcon(url);
                UISpriteInfo spriteInfo = new UISpriteInfo(image, sliceRect);
                atlas.put(spritename, spriteInfo);
            }
            catch(Exception e)
            {
                Debug.LogWarningFormat("UIAtlas.AddSprite - Error while reading sprite %s from %s.\n%s", spritename, url.getFile(), e.getMessage());
                continue;
            }
            return;
        }
        
        // Failed to load sprite.
        Debug.LogWarningFormat("UIAtlas.AddSprite - Failed to add sprite for %s. Check if the file exists in assets/sprites directory.", spritename);
    }
}
