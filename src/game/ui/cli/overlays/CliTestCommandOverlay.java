/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.overlays;

import game.allocation.InitWithDependency;
import game.entities.UserEntity;
import game.io.store.UserStore;
import game.ui.cli.CliView;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;

/**
 * Special overlay for test-purpose commands.
 * @author jerrykim
 */
public class CliTestCommandOverlay extends CliView {
    
    @InitWithDependency
    private void Init(UserStore userStore, UserEntity user)
    {
        CommandInfo setGold = new CommandInfo("setGold", (args) -> {
            int gold = args.GetInt("amount");
            if(gold < 0)
                gold = 0;
            user.Gold.SetValue(gold);
            userStore.Save();
        });
        setGold.SetDescription("(TEST) Sets user's gold.");
        setGold.SetArgument("amount", ArgumentTypes.Int);
        
        commands.AddCommand(setGold);
        commands.SetEnable(true);
    }
    
    // Test command should always be visible.
    public @Override int GetDepth() { return Integer.MAX_VALUE; }
}
