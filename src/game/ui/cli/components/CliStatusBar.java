/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.rulesets.GamePlayer;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;

/**
 * Status bar of a GamePlayer object.
 * @author jerrykim
 */
public class CliStatusBar extends CliDisplayer {
    
    /**
     * Current player being displayed.
     */
    private GamePlayer player;
    
    
    /**
     * Sets current player to be displayed.
     * @param player 
     */
    public void SetPlayer(GamePlayer player)
    {
        this.player = player;
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int startX = (int)position.X;
        int y = (int)position.Y;
        
        buffer.SetBuffer("[ "+player.GetName()+" ]", startX, y);
        buffer.SetBuffer(DrawHealthBar(), startX + 29, y);
        
        int hp = player.GetCurHealth();
        int maxHp = player.GetMaxHealth();
        buffer.SetBuffer(String.format("%d/%d", hp, maxHp), startX+27, y, Pivot.Right);
    }
    
    /**
     * Draws a health bar in string value.
     */
    private String DrawHealthBar()
    {
        final int blocks = 50;
        double hp = player.GetCurHealth();
        double maxHp = player.GetMaxHealth();
        
        int hpBlock = (int)(blocks * hp / maxHp);
        return String.format(
            "[%s%s]",
            new String(new char[hpBlock]).replace('\0', '▓'),
            new String(new char[blocks - hpBlock]).replace('\0', '░')
        );
    }
}
