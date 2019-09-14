/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.commands.CommandInfo;

/**
 * A button displayer.
 * @author jerrykim
 */
public class CliButton extends CliDisplayer {
    
    private int width;
    private int height;
    private String text;
    
    
    public CliButton()
    {
        commands.SetEnable(true);
    }
    
    public CliButton(String text)
    {
        this();
        this.width = text.length() + 2;
        this.height = 3;
        this.text = text;
    }
    
    public CliButton(int width, int height)
    {
        this();
        this.width = width;
        this.height = height;
    }
    
    public CliButton(int width, int height, String text)
    {
        this();
        this.width = width;
        this.height = height;
        this.text = text;
    }
    
    /**
     * Binds the specified command to this button
     * @param command 
     */
    public void BindCommand(CommandInfo command)
    {
        commands.AddCommand(command);
    }
    
    /**
     * Sets the text value to be displayed on the button.
     */
    public void SetText(String text)
    {
        this.text = text;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int x = (int)position.X;
        int y = (int)position.Y;
        int toX = x + width - 1;
        int toY = y + height - 1;
        
        for(int i=x+1; i<toX; i++)
            buffer.SetBuffer('─', i, y);
        for(int i=x+1; i<toX; i++)
            buffer.SetBuffer('─', i, toY);
        for(int i=y+1; i<toY; i++)
        {
            buffer.SetBuffer('│', x, i);
            buffer.SetBuffer('│', toX, i);
        }
        buffer.SetBuffer('┌', x, y);
        buffer.SetBuffer('┐', toX, y);
        buffer.SetBuffer('┘', toX, toY);
        buffer.SetBuffer('└', x, toY);
        
        buffer.SetBuffer(text, (toX+x) / 2, (toY+y) / 2, Pivot.Center);
    }
}
