/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.ui.Pivot;
import java.util.StringTokenizer;

/**
 * Char array wrapper which is used for displaying purposes.
 * The buffer has a simulated width and height which allows the user to render characters at certain position.
 * @author jerrykim
 */
public class CliBuffer {
    
    /**
     * Number of new lines to be inserted in the beginning before actually displaying the content.
     */
    private static final int TopPadding = 8;
    
    /**
     * Buffer value used for padding.
     */
    public final char[] PaddingBuffer;
    
    /**
     * Internal buffer array.
     */
    private char[] buffer;
    
    /**
     * Original buffer which can be used to clear the current buffer.
     */
    private char[] clearBuffer;
    
    /**
     * Simulated application width.
     */
    private int width;
    
    /**
     * Simulated application height.
     */
    private int height;
    
    /**
     * Whether the buffer is dirty and needs redraw.
     */
    private boolean isDirty = false;
    
    
    public CliBuffer(int width, int height)
    {
        this.width = width;
        this.height = height;
        
        PaddingBuffer = new char[TopPadding];
        for(int i=0; i<PaddingBuffer.length; i++)
            PaddingBuffer[i] = '\n';
        
        buffer = new char[(width + 1) * height];
        
        // Setup clear buffer
        clearBuffer = new char[buffer.length];
        for(int r=0; r<height; r++)
        {
            int rInx = r * (width+1);
            for(int c=0; c<width; c++)
                clearBuffer[rInx+c] = ' ';
            clearBuffer[rInx + width] = '\n';
        }
        
        // Set initial buffer data
        Clear();
    }
    
    /**
     * Clears all buffers to initial state.
     */
    public void Clear()
    {
        System.arraycopy(clearBuffer, 0, buffer, 0, buffer.length);
    }
    
    /**
     * Returns the internal buffer reference.
     */
    public char[] GetRawBuffers() { return buffer; }
    
    /**
     * Sets dirty flag on the buffer to indicate whether buffers should be redrawn.
     * @param isDirty 
     */
    public void SetDirty(boolean isDirty) { this.isDirty = isDirty; }
    
    /**
     * Returns whether the buffer is dirty.
     */
    public boolean IsDirty() { return isDirty; }
    
    /**
     * Sets the character at specified location.
     * Attempt to set the character out of bounds will be ignored.
     * @param ch
     * @param x Between 0 and width-1
     * @param y Between 0 and height-1
     */
    public void SetBuffer(char ch, int x, int y)
    {
        if(x < 0 || x >= width || y < 0 || y >= height)
            return;
        // Convert simulated x y position to one dimensional position.
        int inx = x + y * (width+1);
        // If buffer to be set is a different value, we should update.
        if(buffer[inx] != ch)
        {
            if(!isDirty)
            {
                isDirty = true;
                Clear();
            }
            buffer[inx] = ch;
        }
    }
    
    /**
     * Sets buffer using specified string.
     * Note that line breaking is not supported.
     * Default pivot: Left.
     * @param str
     * @param x
     * @param y 
     */
    public void SetBuffer(String str, int x, int y)
    {
        SetBuffer(str, x, y, Pivot.Left);
    }
    
    /**
     * Sets buffer using specified string.
     * Note that line breaking is not supported.
     * @param str
     * @param x
     * @param y
     * @param pivot 
     */
    public void SetBuffer(String str, int x, int y, Pivot pivot)
    {
        if(str == null)
            return;
        int strLen = str.length();
        switch(pivot)
        {
        case Top:
        case Center:
        case Bottom:
            x -= strLen / 2;
            break;
        case TopRight:
        case Right:
        case BottomRight:
            x -= strLen - 1;
            break;
        }
        
        for(int i=0; i<strLen; i++)
            SetBuffer(str.charAt(i), x + i, y);
    }
    
    /**
     * Returns the width of the console canvas.
     */
    public int GetWidth() { return width; }
    
    /**
     * Returns the height of the console canvas.
     */
    public int GetHeight() { return height; }
    
    /**
     * Returns the half width of the console canvas.
     */
    public int GetHalfWidth() { return width / 2; }
    
    /**
     * Returns the half height of the console canvas.
     */
    public int GetHalfHeight() { return height / 2; }
}
