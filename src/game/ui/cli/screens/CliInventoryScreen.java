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
 * Inventory screen.
 * @author jerrykim
 */
public class CliInventoryScreen extends CliView {
    
    private final int ItemsPerPage = 8;
    
    private CliButton homeButton;
    private CliButton shopButton;
    private CliButton nextButton;
    private CliButton prevButton;
    private CliButton pageButton;
    
    private int pageInx;
    
    private String statusMessage;
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    @ReceivesDependency
    private UserStore userStore;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private ItemDefinitions items;
    
    @ReceivesDependency
    private CliOverlayController overlays;
    
    
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
        shopButton = (CliButton)AddChild(new CliButton(15, 3, "Shop"));
        {
            CommandInfo inventory = new CommandInfo("shop", (args) -> {
                screens.ShowView(CliShopScreen.class);
            });
            inventory.SetDescription("Navigates back to shop screen.");
            shopButton.BindCommand(inventory);
            shopButton.SetPosition(19, buttonY);
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
        
        CommandInfo sell = new CommandInfo("sell", (args) -> {
            SellItem(args.GetInt("itemNumber"), args.GetInt("count"), true);
        });
        sell.SetDescription("Sells an item of specified item number.");
        sell.SetArgument("itemNumber", ArgumentTypes.Int)
            .SetArgument("count", ArgumentTypes.Int);
        
        CommandInfo sell_yes = new CommandInfo("sell -y", (args) -> {
            SellItem(args.GetInt("itemNumber"), args.GetInt("count"), false);
        });
        sell_yes.SetDescription("Sells an item of specified item number without additional confirmation.");
        sell_yes.SetArgument("itemNumber", ArgumentTypes.Int)
            .SetArgument("count", ArgumentTypes.Int);
        
        commands.AddCommand(sell);
        commands.AddCommand(sell_yes);
        commands.SetPropagate(false);
    }
    
    public @Override void OnEnable()
    {
        SetPage(0);
        SetStatus(null);
    }
    
    public @Override void Render(CliBuffer buffer)
    {
        int inx = 0;
        for(ItemInfo item : items.GetInfoPage(ItemsPerPage, pageInx))
        {
            ShowItem(buffer, item, inx);
            inx ++;
        }
        
        buffer.SetBuffer(String.format("Page [%d/%d]", pageInx+1, GetMaxPage()), 96, 17, Pivot.Right);
        
        if(statusMessage != null && statusMessage.length() > 0)
            buffer.SetBuffer("--- " + statusMessage + " ---", buffer.GetHalfWidth(), 3, Pivot.Center);
        
        super.Render(buffer);
    }
    
    /**
     * Sells owned items of specified item id.
     * @param id
     * @param count
     * @param confirm 
     */
    private void SellItem(int id, int count, boolean confirm)
    {
        ItemInfo item = items.GetInfo(id);
        if(count <= 0)
        {
            SetStatus("Invalid count!");
            return;
        }
        else if(count > itemStore.GetItemCount(item))
        {
            SetStatus("You don't have enough items!");
            return;
        }
        else if(item == null)
        {
            SetStatus("There is no item of specified id: " + id);
            return;
        }
        
        int price = item.SellPrice * count;
        
        // Define shared sell action for case with confirmation and another without confirmation.
        Action sell = () -> {
            for(int i=0; i<count; i++)
                itemStore.RemoveItem(item);
            user.SetGold(user.GetGold() + price);
            SetStatus("Sold " + count + " " + item.Name + " successfully.");
            
            itemStore.Save();
            userStore.Save();
        };
        
        // Do confirmation process if required.
        if(confirm)
        {
            CliDialogOverlay overlay = overlays.ShowView(CliDialogOverlay.class);
            CliDialogPresets.SetYesNo(
                overlay,
                "Are you sure you want to sell " + count + " " + item.Name + " for " + price + " gold?",
                (a) -> sell.Invoke(),
                null
            );
        }
        else
        {
            sell.Invoke();
        }
    }
    
    /**
     * Shows owned item details for specified item info.
     * @param buffer
     * @param item
     * @param index 
     */
    private void ShowItem(CliBuffer buffer, ItemInfo item, int index)
    {
        int halfWidth = buffer.GetHalfWidth();
        int startX = (index % 2) * (halfWidth-3) + 3;
        int startY = ((index/2) % 4) * 3 + 4;
        int contentY = startY + 1;
        int endX = startX + (halfWidth-4);
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
        
        buffer.SetBuffer(String.format("%d.[%s] (%d)", item.Id, item.Name, itemStore.GetItemCount(item)), (endX + startX) / 2, contentY, Pivot.Center);
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
