/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.entities.User;
import game.io.store.UserStore;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;

/**
 * Registration screen when there is no user data.
 * @author jerrykim
 */
public class CliRegisterScreen extends CliView {
    
    @InitWithDependency
    void Init(CliScreenController screens, UserStore userStore, User user)
    {
        CommandInfo create = new CommandInfo("create", (args) -> {
            String username = args.Get("username");
            if(username != null && !username.isEmpty())
            {
                if(username.length() > 16)
                    username = username.substring(0, 16);
                user.SetUsername(username);
                userStore.Save();
                screens.ShowView(CliMainScreen.class);
            }
        });
        create.SetDescription("Creates a new user profile. (* Max 16 characters *)")
                .SetArgument("username", ArgumentTypes.String, "^[a-zA-Z0-9]+$", "Alphanumeric value without spaces.");
        
        CommandInfo cancel = new CommandInfo("cancel", (args) -> {
            screens.ShowView(CliHomeScreen.class);
        });
        cancel.SetDescription("Returns back to home screen.");
        
        commands.AddCommand(create);
        commands.AddCommand(cancel);
        commands.SetEnable(true);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int halfWidth = buffer.GetHalfWidth();
        int halfHeight = buffer.GetHalfHeight();
        
        buffer.SetBuffer("You don't have a profile yet.", halfWidth, halfHeight, Pivot.Center);
        
        super.Render(buffer);
    }
    
    
}
