/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.Debug;
import game.entities.User;
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
import game.ui.cli.overlays.CliTopMenuOverlay;

/**
 * Main menu screen.
 * @author jerrykim
 */
public class CliMainScreen extends CliView {
    
    private CliButton playButton;
    private CliButton shopButton;
    private CliButton quitButton;
    
    private CliPlayerPortrait portrait;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    @ReceivesDependency
    private User user;
    
    
    @InitWithDependency
    private void Init(ICliEngine engine, CliScreenController screens,
            CliOverlayController overlays)
    {
        playButton = (CliButton)AddChild(new CliButton(16, 3, "Play"));
        {
            CommandInfo play = new CommandInfo("play", (args) -> {
                // TODO: Navigate to mode screen
            });
            playButton.BindCommand(play);
            playButton.SetPosition(3, 16);
        }
        shopButton = (CliButton)AddChild(new CliButton(16, 3, "Shop"));
        {
            CommandInfo shop = new CommandInfo("shop", (args) -> {
                screens.ShowView(CliShopScreen.class);
            });
            shopButton.BindCommand(shop);
            shopButton.SetPosition(20, 16);
        }
        quitButton = (CliButton)AddChild(new CliButton(16, 3, "Quit"));
        {
            CommandInfo quit = new CommandInfo("quit", (args) -> {
                CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
                overlay.SetDialog(
                    "Are you sure you want to quit?",
                    new CommandInfo("yes", (arguments) -> {
                        engine.StopUpdate();
                    }),
                    new CommandInfo("no", null)
                );
            });
            quitButton.BindCommand(quit);
            quitButton.SetPosition(37, 16);
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
        buffer.SetBuffer("[== Power level: " + stats.GetPowerLevel() + " ==]", 24, 5);
        buffer.SetBuffer("[-- Attack value: " + (int)stats.GetPowerValue() + " --]", 24, 6);
        buffer.SetBuffer("[== Armor level: " + stats.GetArmorLevel() + " ==]", 24, 7);
        buffer.SetBuffer("[-- Defense value: " + (int)stats.GetArmorValue()+ " --]", 24, 8);
        buffer.SetBuffer("[== Endurance level: " + stats.GetArmorLevel() + " ==]", 24, 9);
        buffer.SetBuffer("[-- Max health value: " + (int)stats.GetMaxHealth()+ " --]", 24, 10);
        buffer.SetBuffer("[== Luck level: " + stats.GetLuckLevel() + " ==]", 24, 11);
        buffer.SetBuffer("[-- Critical chance value: " + (int)stats.GetCriticalChance()+ "% --]", 24, 12);
        buffer.SetBuffer("[== Fortune level: " + stats.GetFortuneLevel()+ " ==]", 24, 13);
        buffer.SetBuffer("[-- Reward mult. value: " + (int)(100 * stats.GetFortuneValue()) + "% --]", 24, 14);
        
        super.Render(buffer);
    }
}
