/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.graphics;

import java.awt.Font;
import java.util.HashMap;

/**
 * Stores and provides fonts generated during game.
 * @author jerrykim
 */
public class GuiFontProvider {
    
    /**
     * Name of the default font to use.
     */
    public static final String DefaultFont = "Calibri";
    
    
    /**
     * Table of cached fonts.
     */
    private HashMap<Integer, Font> fonts = new HashMap<>();;
    
    
    public GuiFontProvider()
    {
    }
    
    public Font Get(int size) { return Get(DefaultFont, Font.PLAIN, size); }
    
    public Font Get(int style, int size) { return Get(DefaultFont, style, size); }
    
    public Font Get(String font, int style, int size)
    {
        int hash = GetHash(font, style, size);
        // Return cached font if exists.
        if(fonts.containsKey(hash))
            return fonts.get(hash);
        
        // Create new font if not cached.
        Font f = new Font(font, style, size);
        fonts.put(hash, f);
        return f;
    }
    
    /**
     * Calculates the hash code of specified options for mapping.
     */
    private int GetHash(String name, int style, int fontSize)
    {
        return name.hashCode() * (style + 1) * (fontSize + 1);
    }
}
