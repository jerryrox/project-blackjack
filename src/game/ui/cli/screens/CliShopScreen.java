/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.rulesets.ItemDefinitions;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;

/**
 * Shop screen.
 * @author jerrykim
 */
public class CliShopScreen extends CliView {
    
    private final int ItemsPerPage = 6;
    
    private CliButton homeButton;
    private CliButton invenButton;
    private CliButton nextButton;
    private CliButton prevButton;
    
    private int pageInx;
    
    @ReceivesDependency
    private ItemDefinitions items;
    
    
    @InitWithDependency
    private void Init(CliScreenController screens)
    {
        homeButton = (CliButton)AddChild(new CliButton(16, 3, "Home"));
        {
            CommandInfo home = new CommandInfo("home", (args) -> {
                screens.ShowView(CliMainScreen.class);
            });
            homeButton.BindCommand(home);
            homeButton.SetPosition(3, 16);
        }
        invenButton = (CliButton)AddChild(new CliButton(16, 3, "Inventory"));
        {
            CommandInfo inventory = new CommandInfo("inventory", (args) -> {
                screens.ShowView(CliInventoryScreen.class);
            });
            invenButton.BindCommand(inventory);
            invenButton.SetPosition(20, 16);
        }
        nextButton = (CliButton)AddChild(new CliButton(16, 3, "Next page"));
        {
            CommandInfo next = new CommandInfo("next", (args) -> {
                SetPage(pageInx + 1);
            });
            nextButton.BindCommand(next);
            nextButton.SetPosition(37, 16);
        }
        prevButton = (CliButton)AddChild(new CliButton(16, 3, "Prev page"));
        {
            CommandInfo prev = new CommandInfo("prev", (args) -> {
                SetPage(pageInx - 1);
            });
            prevButton.BindCommand(prev);
            prevButton.SetPosition(54, 16);
        }
        
        commands.SetPropagate(false);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        super.Render(buffer);
    }
    
    /**
     * Sets current page index.
     * @param pageInx 
     */
    private void SetPage(int pageInx)
    {
        int maxPage = (items.GetCount() - 1) / ItemsPerPage;
        if(pageInx < 0 || pageInx > maxPage)
            return;
        
        this.pageInx = pageInx;
    }
}
