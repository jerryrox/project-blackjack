/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.CommandInfo;

/**
 * Help screen.
 * @author jerrykim
 */
public class CliHelpScreen extends CliView {
    
    @InitWithDependency
    private void Init(CliScreenController screens)
    {
        CommandInfo home = new CommandInfo("home", (args) -> {
            screens.ShowView(CliMainScreen.class);
        });
        home.SetDescription("Navigates back to home screen.");
        commands.AddCommand(home);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int halfWidth = buffer.GetHalfWidth();
        
        buffer.SetBuffer("How to play", halfWidth, 4, Pivot.Center);
        
        buffer.SetBuffer("Each player starts with one random card which is not visible to the opponent.", halfWidth, 6, Pivot.Center);
        buffer.SetBuffer("The players will share the same deck, with cards ranging from 1 to 11. No doubles.", halfWidth, 7, Pivot.Center);
        buffer.SetBuffer("Players take turns drawing a card to reach 21 or close below 21.", halfWidth, 8, Pivot.Center);
        buffer.SetBuffer("Exceeding the total of 21 is considered losing.", halfWidth, 9, Pivot.Center);
        buffer.SetBuffer("When both players decide to skip turn, the phase is evaluated for the winner.", halfWidth, 10, Pivot.Center);
        buffer.SetBuffer("", halfWidth, 11, Pivot.Center);
        buffer.SetBuffer("The winner inflicts their damage stat to the opponent with some critical chance.", halfWidth, 12, Pivot.Center);
        buffer.SetBuffer("If the phase ended in a draw, both players receive damage.", halfWidth, 13, Pivot.Center);
        buffer.SetBuffer("", halfWidth, 14, Pivot.Center);
        buffer.SetBuffer("* Damaging when the card total is 21 has 200% critical chance. *", halfWidth, 15, Pivot.Center);
        buffer.SetBuffer("* Critical strike inflicts 150% damage. *", halfWidth, 16, Pivot.Center);
        buffer.SetBuffer("", halfWidth, 17, Pivot.Center);
    }
}
