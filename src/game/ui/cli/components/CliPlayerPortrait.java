/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;

/**
 * Displays the player portrait.
 * @author jerrykim
 */
public class CliPlayerPortrait extends CliDisplayer {
    
    public CliPlayerPortrait(int x, int y)
    {
        position.X = x;
        position.Y = y;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int x = (int)position.X;
        int y = (int)position.Y;
        buffer.SetBuffer("  _____  ", x, y++);
        buffer.SetBuffer(" /_   _\\ ", x, y++);
        buffer.SetBuffer("| 0 w 0 |", x, y++);
        buffer.SetBuffer(" \\_____/ ", x, y++);
        buffer.SetBuffer("    |    ", x, y++);
        buffer.SetBuffer("   /|\\   ", x, y++);
        buffer.SetBuffer("  / | \\  ", x, y++);
        buffer.SetBuffer(" /  |  \\ ", x, y++);
        buffer.SetBuffer("   / \\   ", x, y++);
        buffer.SetBuffer("  /   \\  ", x, y++);
        buffer.SetBuffer(" /     \\ ", x, y++);
    }
}
