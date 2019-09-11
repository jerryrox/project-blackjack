/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components.ui;

import game.allocation.ReceivesDependency;
import game.data.Rect;
import game.data.Vector2;
import game.debug.Debug;
import game.debug.ILogger;
import game.ui.gui.graphics.UIAtlas;
import game.ui.gui.graphics.UISpriteInfo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;

/**
 * Displays an image cached in UIAtlas instance.
 * @author jerrykim
 */
public class UISprite extends UIWidget {

    /**
     * Current spritename of sprite in use.
     */
    private String curSpritename = null;
    
    /**
     * The image source which is used to build the buffered image.
     */
    private ImageIcon curIcon;
    
    /**
     * Current image to be drawn.
     */
    private BufferedImage curImage = null;
    
    /**
     * Slice rect value of image.
     */
    private Rect sliceRect = null;
    
    /**
     * Size of the original image.
     */
    private Vector2 imageSize = new Vector2();
    
    /**
     * Sprite wrap mode.
     */
    private WrapModes wrapMode = WrapModes.Simple;
    
    /**
     * Whether the image itself has changed when isDirty became true.
     * This would indicate reinstantiation of BufferedImage to switch the sprite.
     */
    private boolean isImageDirty = true;
    
    /**
     * An array used for sliced sprite drawing.
     */
    private Vector2[] destDrawSizes = new Vector2[] {
        new Vector2(), new Vector2(), new Vector2()
    };
    
    /**
     * An array used for sliced sprite drawing.
     */
    private Vector2[] srcDrawSizes = new Vector2[] {
        new Vector2(), new Vector2(), new Vector2()
    };
    
    /**
     * Holds array of previous color values for performance issues.
     */
    private float[] prevColor = new float[] { -1, -1, -1, -1 };
    
    /**
     * Due to Java GUI's limitations, applying multiplicative blending continuously outputs unwanted results
     * when image has an alpha channel.
     * This array will contain the last color buffers used before applying blending so we have an approximate color
     * to return to.
     */
    private int[][] lastColorBuffer = null;
    private boolean lcbCached = false;
    
    @ReceivesDependency
    private UIAtlas atlas;
    
    @ReceivesDependency
    private ILogger logger;
    
    
    public UISprite()
    {
        super();
    }
    
    /**
     * Sets the spritename to display the image associated with it.
     */
    public void SetSpritename(String spritename)
    {
        if(curSpritename != null && curSpritename.equals(spritename))
            return;
        curSpritename = spritename;
        
        UISpriteInfo info = atlas.GetSprite(spritename);
        if(info == null)
        {
            curImage = null;
            sliceRect = null;
            logger.LogWarningFormat("UISprite.SetSpritename - No sprite info was found for name (%s). Check if this spritename actually exists.", spritename);
            return;
        }
        
        sliceRect = info.GetSliceRect();
        
        curIcon = info.GetImage();
        imageSize.X = curIcon.getIconWidth();
        imageSize.Y = curIcon.getIconHeight();
        
        isImageDirty = true;
    }
    
    /**
     * Returns the current spritename in use.
     */
    public String GetSpritename() { return curSpritename; }
    
    /**
     * Sets sprite wrap mode.
     */
    public void SetWrapMode(WrapModes mode) { wrapMode = mode; }
    
    /**
     * Returns the current wrap mode in use.
     */
    public WrapModes GetWrapMode() { return wrapMode; }
    
    public @Override void ResetSize() { SetSize(imageSize); }
    
    protected @Override void Draw(Graphics buffer, Vector2 drawPos, Vector2 scale, Vector2 actualSize)
    {
        if(curIcon == null)
            return;
        
        // Resolve dirtiness if necessary.
        RebuildSprite();
        
        if(curImage == null)
            return;
        
        if(wrapMode == WrapModes.Simple || sliceRect.IsZero())
        {
            buffer.drawImage(curImage, (int)drawPos.X, (int)drawPos.Y, (int)actualSize.X, (int)actualSize.Y, rootPanel);
        }
        else
        {
            srcDrawSizes[0].X = sliceRect.X;
            srcDrawSizes[0].Y = sliceRect.Y;
            srcDrawSizes[1].X = sliceRect.Z;
            srcDrawSizes[1].Y = sliceRect.W;
            srcDrawSizes[2].X = imageSize.X - sliceRect.X - sliceRect.Z;
            srcDrawSizes[2].Y = imageSize.Y - sliceRect.Y - sliceRect.W;
            
            destDrawSizes[0].X = srcDrawSizes[0].X * scale.X;
            destDrawSizes[0].Y = srcDrawSizes[0].Y * scale.Y;
            destDrawSizes[2].X = srcDrawSizes[2].X * scale.X;
            destDrawSizes[2].Y = srcDrawSizes[2].Y * scale.Y;
            destDrawSizes[1].X = actualSize.X - destDrawSizes[0].X - destDrawSizes[2].X;
            destDrawSizes[1].Y = actualSize.Y - destDrawSizes[0].Y - destDrawSizes[2].Y;
            
            int destY = (int)drawPos.Y;
            int srcY = 0;
            for(int r=0; r<3; r++)
            {
                int destX = (int)drawPos.X;
                int srcX = 0;
                for(int c=0; c<3; c++)
                {
                    buffer.drawImage(curImage,
                        destX, destY, destX + (int)destDrawSizes[c].X, destY + (int)destDrawSizes[r].Y,
                        srcX, srcY, srcX + (int)srcDrawSizes[c].X, srcY + (int)srcDrawSizes[r].Y,
                        rootPanel
                    );
                    destX += (int)destDrawSizes[c].X;
                    srcX += (int)srcDrawSizes[c].X;
                }
                destY += (int)destDrawSizes[r].Y;
                srcY += (int)srcDrawSizes[r].Y;
            }
        }
    }
    
    /**
     * Rebuilds buffered image.
     */
    private void RebuildSprite()
    {
        // Rebuild the image itself if necessary.
        if(isImageDirty)
        {
            curImage = new BufferedImage((int)imageSize.X, (int)imageSize.Y, BufferedImage.TYPE_INT_ARGB);
            lastColorBuffer = new int[curImage.getWidth()][curImage.getHeight()];
            lcbCached = false;
        }
        
        Color color = GetColor();
        final float byteReciprocal = 1f / 255f;
        float a = GetWorldAlpha();
        float r = color.getRed() * byteReciprocal;
        float g = color.getGreen() * byteReciprocal;
        float b = color.getBlue() * byteReciprocal;
        
        // If no change in color or sprite, just return.
        if(!isImageDirty && a == prevColor[0] && r == prevColor[1] && g == prevColor[2] && b == prevColor[3])
            return;
        prevColor[0] = a;
        prevColor[1] = r;
        prevColor[2] = g;
        prevColor[3] = b;
        
        // Redraw image if necessary.
        Graphics2D graphic = curImage.createGraphics();
        graphic.drawImage(curIcon.getImage(), 0, 0, null);
        graphic.dispose();
        
        // Tinting the sprite.
        // Due to limitations of Java Swing's GUI system, each pixel must be tinted manually :(
        ColorModel colorModel = curImage.getColorModel();
        WritableRaster raster = curImage.getRaster();
        for (int i = 0; i < curImage.getWidth(); i++)
        {
            for (int j = 0; j < curImage.getHeight(); j++)
            {
                // Cache original color value.
                if(!lcbCached)
                    lastColorBuffer[i][j] = (colorModel.getAlpha(raster.getDataElements(i, j, null)) << 24) |
                            (colorModel.getRed(raster.getDataElements(i, j, null)) << 16) |
                            (colorModel.getGreen(raster.getDataElements(i, j, null)) << 8) |
                            colorModel.getBlue(raster.getDataElements(i, j, null));
                
                long originalColor = lastColorBuffer[i][j];
                int ax = (int)(((originalColor >> 24) & 0x000000ff) * a);
                int rx = (int)(((originalColor >> 16) & 0x000000ff) * a);
                int gx = (int)(((originalColor >> 8) & 0x000000ff) * a);
                int bx = (int)((originalColor & 0x000000ff) * a);
                
                curImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | bx);
            }
        }
        
        lcbCached = true;
        isImageDirty = false;
    }
    
    /**
     * Types of methods used for drawing the sprite.
     */
    public enum WrapModes {
        Simple,
        Sliced
    }
}
