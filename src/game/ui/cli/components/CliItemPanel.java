/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli.components;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.entities.OwnedItemModel;
import game.io.store.ItemStore;
import game.rulesets.BaseRuleset;
import game.rulesets.Deck;
import game.rulesets.GameAIPlayer;
import game.rulesets.GamePlayer;
import game.rulesets.items.CloneItemInfo;
import game.rulesets.items.ItemInfo;
import game.rulesets.items.PeekItemInfo;
import game.rulesets.items.ReturnItemInfo;
import game.rulesets.items.RevealItemInfo;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;
import java.util.ArrayList;

/**
 * Item use panel.
 * @author jerrykim
 */
public class CliItemPanel extends CliDisplayer {
    
    private final int ItemsPerPage = 14;
    
    private CliFrame frame;
    
    private ArrayList<ItemEntry> entries = new ArrayList<ItemEntry>();
    
    private int curPage;
    private String statusMessage;
    
    private BaseRuleset ruleset;
    private CliGameInfoPanel infoPanel;
    
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    
    @InitWithDependency
    private void Init()
    {
        AddChild(frame = new CliFrame());
        
        CommandInfo close = new CommandInfo("close", (args) -> {
            SetActive(false);
        });
        close.SetDescription("Closes this panel.");
        
        CommandInfo use = new CommandInfo("use", (args) -> {
            UseItem(args.GetInt("itemNumber"));
        });
        use.SetDescription("Uses the item of specified item number.");
        use.SetArgument("itemNumber", ArgumentTypes.Int);
        
        commands.AddCommand(close);
        commands.AddCommand(use);
        
        // Temporarily disable game commands
        commands.SetPropagate(false);
    }
    
    /**
     * Sets the context which the item is used in.
     * @param infoPanel
     * @param ruleset
     */
    public void SetContext(CliGameInfoPanel infoPanel, BaseRuleset ruleset)
    {
        this.infoPanel = infoPanel;
        this.ruleset = ruleset;
    }
    
    public @Override void OnEnable()
    {
        entries.clear();
        for(OwnedItemModel item : itemStore.GetItems())
        {
            ItemEntry entry = FindEntry(item.GetInfo());
            if(entry == null)
                entries.add(entry = new ItemEntry(item.GetInfo()));
            
            entry.Count ++;
        }
        
        SetPage(1);
    }
    
    public @Override int GetDepth() { return 100; }
    
    public @Override void Render(CliBuffer buffer)
    {
        int startX = 1;
        int startY = 1;
        int endX = 83;
        int endY = buffer.GetLastY()-1;
        int centerX = (startX + endX) / 2;
        
        // Clear area
        for(int x=startX; x<=endX; x++)
        {
            for(int y=startY; y<=endY; y++)
                buffer.SetBuffer(' ', x, y);
        }
        
        // Set frame rect
        frame.SetRect(startX, startY, endX, endY);
        
        // Title
        buffer.SetBuffer("Items", centerX, 4, Pivot.Center);
        
        // Status message
        if(statusMessage != null && statusMessage.length() > 0)
            buffer.SetBuffer("[ " + statusMessage + " ]", centerX, 6, Pivot.Center);
        
        // Items
        int itemStartInx = (curPage - 1) * ItemsPerPage;
        int itemEnd = Math.min(itemStartInx + ItemsPerPage, entries.size());
        for(int i=itemStartInx; i<itemEnd; i++)
            DrawItem(buffer, entries.get(i), (i%2) * 38 + 5, (i/2) + 8);
        
        super.Render(buffer);
    }
    
    /**
     * Sets the status message.
     * @param message 
     */
    private void SetStatus(String message) { statusMessage = message; }
    
    /**
     * Sets the next page to be drawn.
     * @param page 
     */
    private void SetPage(int page)
    {
        if(page < 1 || page > GetMaxPage())
            return;
        curPage = page;
    }
    
    /**
     * Returns the max page number.
     */
    private int GetMaxPage()
    {
        return (entries.size() - 1) / ItemsPerPage + 1;
    }
    
    /**
     * Draws the specified item entry.
     */
    private void DrawItem(CliBuffer buffer, ItemEntry entry, int x, int y)
    {
        buffer.SetBuffer('[', x, y);
        buffer.SetBuffer(']', x+36, y);
        buffer.SetBuffer(String.format("%d. %s", entry.Item.Id, entry.Item.Name), x+1, y);
        buffer.SetBuffer("("+entry.Count+")", x+35, y, Pivot.Right);
    }
    
    /**
     * Finds the item entry of specified item info.
     * @param info
     */
    private ItemEntry FindEntry(ItemInfo info)
    {
        for(int i=0; i<entries.size(); i++)
        {
            if(entries.get(i).Item == info)
                return entries.get(i);
        }
        return null;
    }
    
    /**
     * Uses the item of specified id.
     */
    private void UseItem(int id)
    {
        for(int i=0; i<entries.size(); i++)
        {
            ItemEntry entry = entries.get(i);
            if(entry.Item.Id == id)
            {
                GamePlayer humanPlayer = ruleset.GetGameProcessor().GetHumanPlayer();
                GameAIPlayer aiPlayer = ruleset.GetGameProcessor().GetAIPlayer();
                Deck deck = ruleset.GetGameProcessor().GetDeck();
                
                // Check for special utility items
                Class itemClass = entry.Item.getClass();
                if(itemClass == PeekItemInfo.class)
                    infoPanel.SetPeekDeck(true);
                else if(itemClass == RevealItemInfo.class)
                    aiPlayer.SetReveal(true);
                else if(itemClass == ReturnItemInfo.class)
                {
                    humanPlayer.GetHand().ReturnCard(deck);
                    deck.Shuffle();
                }
                else if(itemClass == CloneItemInfo.class)
                    humanPlayer.GetHand().ClearCards();
                else
                    humanPlayer.ApplyItem(entry.Item);
                
                // Use item
                entry.Count --;
                if(entry.Count <= 0)
                    entries.remove(i);
                itemStore.RemoveItem(entry.Item);
                itemStore.Save();
                
                // Show message
                SetStatus("Used " + entry.Item.Name);
                return;
            }
        }
        
        SetStatus("Invalid item number!");
    }
    
    
    /**
     * Represents a single type of item which the player can use.
     */
    private class ItemEntry {
        
        /**
         * Item reference info.
         */
        public ItemInfo Item;
        
        /**
         * Number of owned item instances.
         */
        public int Count;
        
        
        public ItemEntry(ItemInfo item)
        {
            Item = item;
        }
    }
}
