/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.UserEntity;
import game.entities.UserStats;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.ICliEngine;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;
import game.ui.cli.components.CliPlayerPortrait;
import game.ui.cli.overlays.CliDialogOverlay;
import game.ui.cli.overlays.CliDialogPresets;
import game.ui.cli.overlays.CliTopMenuOverlay;

/**
 * Main menu screen.
 * @author jerrykim
 */
public class CliMainScreen extends CliView {
    
    private CliButton playButton;
    private CliButton shopButton;
    private CliButton inventoryButton;
    private CliButton statButton;
    private CliButton helpButton;
    private CliButton quitButton;
    
    private CliPlayerPortrait portrait;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    @ReceivesDependency
    private UserEntity user;
    
    
    @InitWithDependency
    private void Init(ICliEngine engine, CliScreenController screens,
            CliOverlayController overlays)
    {
        int buttonWidth = 15;
        int buttonX = 3;
        int buttonY = 16;
        playButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Play"));
        {
            CommandInfo play = new CommandInfo("play", (args) -> {
                screens.ShowView(CliModeScreen.class);
            });
            play.SetDescription("Navigates to play mode selection screen.");
            playButton.BindCommand(play);
            playButton.SetPosition(buttonX, buttonY);
        }
        shopButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Shop"));
        {
            CommandInfo shop = new CommandInfo("shop", (args) -> {
                screens.ShowView(CliShopScreen.class);
            });
            shop.SetDescription("Navigates to shop screen.");
            shopButton.BindCommand(shop);
            shopButton.SetPosition(buttonX += buttonWidth + 1, buttonY);
        }
        inventoryButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Inventory"));
        {
            CommandInfo inventory = new CommandInfo("inventory", (args) -> {
                screens.ShowView(CliInventoryScreen.class);
            });
            inventory.SetDescription("Navigates to inventory screen.");
            inventoryButton.BindCommand(inventory);
            inventoryButton.SetPosition(buttonX += buttonWidth + 1, buttonY);
        }
        statButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Stat"));
        {
            CommandInfo stat = new CommandInfo("stat", (args) -> {
                screens.ShowView(CliStatScreen.class);
            });
            stat.SetDescription("Navigates to stat screen.");
            statButton.BindCommand(stat);
            statButton.SetPosition(buttonX += buttonWidth + 1, buttonY);
        }
        helpButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Help"));
        {
            CommandInfo stat = new CommandInfo("help", (args) -> {
                screens.ShowView(CliHelpScreen.class);
            });
            stat.SetDescription("Navigates to help screen.");
            helpButton.BindCommand(stat);
            helpButton.SetPosition(buttonX += buttonWidth + 1, buttonY);
        }
        quitButton = (CliButton)AddChild(new CliButton(buttonWidth, 3, "Quit"));
        {
            CommandInfo quit = new CommandInfo("quit", (args) -> {
                CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
                CliDialogPresets.SetQuit(
                    overlay,
                    (a) -> engine.StopUpdate(),
                    null
                );
            });
            quit.SetDescription("Quits the game.");
            quitButton.BindCommand(quit);
            quitButton.SetPosition(buttonX += buttonWidth + 1, buttonY);
        }
        
        portrait = (CliPlayerPortrait)AddChild(new CliPlayerPortrait(10, 4));
    }
    
    public @Override void OnEnable()
    {
        overlays.ShowView(CliTopMenuOverlay.class);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        // Show stat levels of user.
        UserStats stats = user.GetStats();
        buffer.SetBuffer("Player stats:", 24, 4);
        buffer.SetBuffer("[== Power level: " + stats.Power.GetLevel()+ " ==]", 24, 5);
        buffer.SetBuffer("[-- Attack value: " + (int)stats.Power.GetValue()+ " --]", 24, 6);
        buffer.SetBuffer("[== Armor level: " + stats.Armor.GetLevel() + " ==]", 24, 7);
        buffer.SetBuffer("[-- Defense value: " + (int)stats.Armor.GetValue()+ " --]", 24, 8);
        buffer.SetBuffer("[== Endurance level: " + stats.Endurance.GetLevel()+ " ==]", 24, 9);
        buffer.SetBuffer("[-- Max health value: " + (int)stats.Endurance.GetValue()+ " --]", 24, 10);
        buffer.SetBuffer("[== Luck level: " + stats.Luck.GetLevel() + " ==]", 24, 11);
        buffer.SetBuffer("[-- Critical chance value: " + (int)stats.Luck.GetValue()+ "% --]", 24, 12);
        buffer.SetBuffer("[== Fortune level: " + stats.Fortune.GetLevel()+ " ==]", 24, 13);
        buffer.SetBuffer("[-- Reward mult. value: " + (int)(100 * stats.Fortune.GetValue()) + "% --]", 24, 14);
        
        super.Render(buffer);
    }
}
