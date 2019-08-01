/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.rulesets.ICard;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;

/**
 * Card displayer.
 * @author jerrykim
 */
public class CliCard extends CliDisplayer {
    
    /**
     * Current card instance being displayed.
     */
    private int value;
    
    /**
     * Whether the card value should be hidden.
     */
    private boolean isHidden;
    
    
    /**
     * Sets the value to be displayed.
     */
    public void SetValue(int value) { this.value = value; }
    
    /**
     * Sets the card's visibility state.
     * @param isHidden 
     */
    public void SetHidden(boolean isHidden) { this.isHidden = isHidden; }
    
    public @Override void Render(CliBuffer buffer)
    {
       int startX = (int)position.X;
       int startY = (int)position.Y;
       int endX = startX + 3;
       int endY = startY + 2;
       
       buffer.SetBuffer('┌', startX, startY);
       buffer.SetBuffer('┐', endX, startY);
       buffer.SetBuffer('┘', endX, endY);
       buffer.SetBuffer('└', startX, endY);
       buffer.SetBuffer("--", startX+1, startY);
       buffer.SetBuffer("--", startX+1, endY);
       buffer.SetBuffer('│', startX, startY+1);
       buffer.SetBuffer('│', endX, startY+1);
       
       if(isHidden)
           buffer.SetBuffer("??", startX+1, startY+1);
       else if(value < 10)
           buffer.SetBuffer("0"+value, startX+1, startY+1);
       else
           buffer.SetBuffer(String.valueOf(value), startX+1, startY+1);
    }
}
