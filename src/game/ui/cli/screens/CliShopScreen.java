/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Action;
import game.entities.UserEntity;
import game.io.store.ItemStore;
import game.io.store.UserStore;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliOverlayController;
import game.ui.cli.CliScreenController;
import game.ui.cli.CliView;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;
import game.ui.cli.components.CliButton;
import game.ui.cli.overlays.CliDialogOverlay;
import game.ui.cli.overlays.CliDialogPresets;

/**
 * Shop screen.
 * @author jerrykim
 */
public class CliShopScreen extends CliView {
    
    private final int ItemsPerPage = 4;
    
    private CliButton homeButton;
    private CliButton invenButton;
    private CliButton nextButton;
    private CliButton prevButton;
    private CliButton pageButton;
    
    private int pageInx;
    
    private String statusMessage;
    
    @ReceivesDependency
    private ItemDefinitions items;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    @ReceivesDependency
    private UserStore userStore;
    
    @ReceivesDependency
    private UserEntity user;
    
    
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
        invenButton = (CliButton)AddChild(new CliButton(15, 3, "Inventory"));
        {
            CommandInfo inventory = new CommandInfo("inventory", (args) -> {
                screens.ShowView(CliInventoryScreen.class);
            });
            inventory.SetDescription("Views the items you own.");
            invenButton.BindCommand(inventory);
            invenButton.SetPosition(19, buttonY);
        }
        nextButton = (CliButton)AddChild(new CliButton(15, 3, "Next page"));
        {
            CommandInfo next = new CommandInfo("next", (args) -> {
                SetPage(pageInx + 1);
            });
            next.SetDescription("Navigates to next page.");
            nextButton.BindCommand(next);
            nextButton.SetPosition(35, buttonY);
        }
        prevButton = (CliButton)AddChild(new CliButton(15, 3, "Prev page"));
        {
            CommandInfo prev = new CommandInfo("prev", (args) -> {
                SetPage(pageInx - 1);
            });
            prev.SetDescription("Navigates to previous page.");
            prevButton.BindCommand(prev);
            prevButton.SetPosition(51, buttonY);
        }
        pageButton = (CliButton)AddChild(new CliButton(15, 3, "Set page"));
        {
            CommandInfo page = new CommandInfo("page", (args) -> {
                SetPage(args.GetInt("pageNumber") - 1);
            });
            page.SetArgument("pageNumber", ArgumentTypes.Int);
            page.SetDescription("Navigate to specified page number");
            pageButton.BindCommand(page);
            pageButton.SetPosition(67, buttonY);
        }
        
        CommandInfo buy = new CommandInfo("buy", (args) -> {
            BuyItem(args.GetInt("itemNumber"), args.GetInt("count"), true);
        });
        buy.SetDescription("Purchases an item of specified item number.");
        buy.SetArgument("itemNumber", ArgumentTypes.Int)
                .SetArgument("count", ArgumentTypes.Int);
        
        CommandInfo buy_yes = new CommandInfo("buy -y", (args) -> {
            BuyItem(args.GetInt("itemNumber"), args.GetInt("count"), false);
        });
        buy_yes.SetDescription("Purchases an item of specified item number without additional confirmation.");
        buy_yes.SetArgument("itemNumber", ArgumentTypes.Int)
                .SetArgument("count", ArgumentTypes.Int);
        
        CommandInfo info = new CommandInfo("info", (args) -> {
            ItemInfo item = items.GetInfo(args.GetInt("itemNumber"));
            if(item == null)
                SetStatus("An invalid item number was given!");
            else
                SetStatus(item.Description);
        });
        info.SetDescription("Shows the description of the item of specified item number.");
        info.SetArgument("itemNumber", ArgumentTypes.Int);
        
        commands.AddCommand(buy);
        commands.AddCommand(buy_yes);
        commands.AddCommand(info);
        commands.SetPropagate(false);
    }
    
    public @Override void OnEnable()
    {
        SetPage(0);
        SetStatus(null);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int count = 0;
        for(ItemInfo item : items.GetInfoPage(ItemsPerPage, pageInx))
        {
            ShowItem(buffer, item, count);
            count ++;
        }
        
        buffer.SetBuffer(String.format("Page [%d/%d]", pageInx+1, GetMaxPage()), 96, 17, Pivot.Right);
        
        if(statusMessage != null && statusMessage.length() > 0)
            buffer.SetBuffer("--- " + statusMessage + " ---", buffer.GetHalfWidth(), 3, Pivot.Center);
        
        super.Render(buffer);
    }
    
    /**
     * Sets buffer for a single shop item.
     * @param buffer 
     */
    private void ShowItem(CliBuffer buffer, ItemInfo item, int index)
    {
        int startX = 3;
        int startY = index * 3 + 4;
        int contentY = startY + 1;
        int endX = buffer.GetLastX() - 3;
        int endY = startY + 2;
        
        buffer.SetBuffer('┌', startX, startY);
        buffer.SetBuffer('└', startX, endY);
        buffer.SetBuffer('┐', endX, startY);
        buffer.SetBuffer('┘', endX, endY);
        buffer.SetBuffer('│', startX, startY+1);
        buffer.SetBuffer('│', endX, startY+1);
        for(int i=startX+1; i<endX; i++)
            buffer.SetBuffer('─', i, startY);
        for(int i=startX+1; i<endX; i++)
            buffer.SetBuffer('─', i, endY);
        
        buffer.SetBuffer(String.format("%d.[%s] - %s", item.Id, item.Name, item.Description), 5, contentY);
        buffer.SetBuffer("| " + item.BuyCost + " gold", 94, contentY, Pivot.Right);
    }
    
    /**
     * Tries purchasing the item of specified id.
     * @param id 
     */
    private void BuyItem(int id, int count, boolean confirm)
    {
        ItemInfo item = items.GetInfo(id);
        if(count <= 0)
        {
            SetStatus("Invalid count!");
            return;
        }
        if(item == null)
        {
            SetStatus("There is no item of specified id: " + id);
            return;
        }
        int cost = item.BuyCost * count;
        if(cost > user.Gold.GetValue())
        {
            SetStatus("You don't have enough gold.");
            return;
        }
        
        // Define shared purchase action for case with confirmation and another without confirmation.
        Action purchase = () -> {
            for(int i=0; i<count; i++)
                itemStore.AddItem(item);
            user.Gold.SetValue(user.Gold.GetValue()- cost);
            SetStatus("Purchased " + count + " " + item.Name + " successfully.");
            
            itemStore.Save();
            userStore.Save();
        };
        
        // Do confirmation process if required.
        if(confirm)
        {
            CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
            CliDialogPresets.SetYesNo(
                overlay,
                "Are you sure you want to purchase " + count + " " + item.Name + " for " + cost + " gold?",
                (a) -> purchase.Invoke(),
                null
            );
        }
        else
        {
            purchase.Invoke();
        }
    }
    
    /**
     * Sets current status message.
     * @param message 
     */
    private void SetStatus(String message)
    {
        statusMessage = message;
    }
    
    /**
     * Sets current page index.
     * @param pageInx 
     */
    private void SetPage(int pageInx)
    {
        if(pageInx < 0 || pageInx >= GetMaxPage())
            return;
        
        this.pageInx = pageInx;
    }
    
    /**
     * Returns the max page number.
     * @return 
     */
    private int GetMaxPage()
    {
        return (items.GetCount() - 1) / ItemsPerPage + 1;
    }
}
