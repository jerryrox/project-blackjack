/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Action;
import game.entities.Stat;
import game.entities.UserEntity;
import game.entities.UserStats;
import game.io.store.UserStore;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;

/**
 * Player stat upgrade screen.
 * @author jerrykim
 */
public class CliStatScreen extends CliView {
    
    private CliButton homeButton;
    
    private String statusMessage;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UserStore userStore;
    
    
    @InitWithDependency
    private void Init(CliScreenController screens)
    {
        int buttonY = 16;
        homeButton = (CliButton)AddChild(new CliButton(15, 3, "Home"));
        {
            CommandInfo home = new CommandInfo("home", (args) -> {
                screens.ShowView(CliMainScreen.class);
            });
            home.SetDescription("Navigates back to home screen.");
            homeButton.BindCommand(home);
            homeButton.SetPosition(3, buttonY);
        }
        
        CommandInfo upgrade = new CommandInfo("upgrade", (args) -> {
            Upgrade(args.GetInt("statNumber"));
        });
        upgrade.SetDescription("Upgrades the stat of specified stat number.");
        upgrade.SetArgument("statNumber", ArgumentTypes.Int);
        
        commands.AddCommand(upgrade);
    }
    
    public @Override void OnEnable()
    {
        SetStatus(null);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        UserStats stats = user.GetStats();
        ShowStat(buffer, stats.Power, 0);
        ShowStat(buffer, stats.Armor, 1);
        ShowStat(buffer, stats.Endurance, 2);
        ShowStat(buffer, stats.Luck, 3);
        ShowStat(buffer, stats.Fortune, 4);
        
        if(statusMessage != null && statusMessage.length() > 0)
            buffer.SetBuffer("--- " + statusMessage + " ---", buffer.GetHalfWidth(), 3, Pivot.Center);
        
        super.Render(buffer);
    }
    
    /**
     * Upgrades the stat of specified stat number.
     */
    private void Upgrade(int statNumber)
    {
        Stat stat = null;
        switch(statNumber)
        {
        case 1:
            stat = user.GetStats().Power;
            break;
        case 2:
            stat = user.GetStats().Armor;
            break;
        case 3:
            stat = user.GetStats().Endurance;
            break;
        case 4:
            stat = user.GetStats().Luck;
            break;
        case 5:
            stat = user.GetStats().Fortune;
            break;
        default:
            SetStatus("Item was not found for given number!");
            return;
        }
        if(user.Gold.GetValue()< stat.GetCost())
        {
            SetStatus("You don't have enough gold!");
            return;
        }
        
        // Do upgrade and decrease gold
        user.Gold.SetValue(user.Gold.GetValue() - stat.GetCost());
        stat.SetLevel(stat.GetLevel() + 1);
        userStore.Save();
        
        // Set status message
        SetStatus("Upgraded " + stat.GetName() + " to level " + stat.GetLevel());
    }
    
    /**
     * Shows stat display using specified options.
     * @param buffer 
     */
    private void ShowStat(CliBuffer buffer, Stat stat, int inx)
    {
        int startX = 3;
        int startY = inx * 2 + 5;
        buffer.SetBuffer(String.format("#%d [%s]", inx+1, stat.GetName()), startX, startY);
        buffer.SetBuffer("Level: " + stat.GetLevel(), startX + 16, startY);
        buffer.SetBuffer("Upgrade cost: " + stat.GetCost(), startX + 28, startY);
        buffer.SetBuffer("- " + stat.GetDescription(), startX + 4, startY + 1);
    }
    
    /**
     * Sets current status message value.
     * @param message 
     */
    private void SetStatus(String message) { statusMessage = message; }
}
