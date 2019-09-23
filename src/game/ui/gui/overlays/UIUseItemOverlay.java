/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Action;
import game.data.Paginator;
import game.debug.Debug;
import game.entities.OwnedItemEntity;
import game.graphics.ColorPreset;
import game.io.store.ItemStore;
import game.rulesets.items.ItemInfo;
import game.rulesets.ui.gui.controller.GuiGameHumanController;
import game.ui.gui.UIOverlayController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.customized.UIInventoryItem;
import game.ui.gui.objects.customized.UIItemList;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 * Overlay for using items.
 * @author jerrykim
 */
public class UIUseItemOverlay extends UIOverlay {
    
    private UIItemList<UIInventoryItem> itemList;
    private Paginator<Entry> paginator;
    
    private UILabel pageLabel;
    
    private boolean usingItem = false;
    
    private GuiGameHumanController controller;
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    
    public UIUseItemOverlay()
    {
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite bg = uiObject.CreateChild().AddComponent(new UISprite());
        {
            bg.GetObject().SetDepth(-1);
            bg.SetSpritename("box");
            bg.SetColor(Color.black);
            bg.SetAlpha(0.75f);
            bg.SetSize(1280, 720);
        }
        
        UILabel title = uiObject.CreateChild().AddComponent(new UILabel());
        {
            title.SetText("Select an item to use.");
            title.GetTransform().SetLocalPosition(0, -320);
            title.SetFont(Font.BOLD, 24);
        }
        
        final int itemsPerPage = 9;
        paginator = new Paginator<>(itemsPerPage);
        uiObject.AddChild(itemList = new UIItemList<>(itemsPerPage, () -> new UIInventoryItem()));
        {
            itemList.GetTransform().SetLocalPosition(0, -240);
            itemList.SetCallback((item) -> {
                UseItem(item);
            });
        }
        
        final float menuPosY = 320;
        UIRoundBoxButton closeButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            closeButton.SetBgColor(colors.Warning);
            closeButton.SetWidth(200);
            closeButton.SetLabel("Close");
            closeButton.SetTextColor(colors.Dark);
            closeButton.GetTransform().SetLocalPosition(480, menuPosY);
            closeButton.Clicked.Add((arg) -> {
                overlays.HideView(this);
            });
        }
        
        pageLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            pageLabel.GetTransform().SetLocalPosition(-330, menuPosY);
        }
        UIRoundBoxButton prevButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            prevButton.SetBgColor(colors.Positive);
            prevButton.SetWidth(180);
            prevButton.SetLabel("Prev page");
            prevButton.GetTransform().SetLocalPosition(-480, menuPosY);
            prevButton.Clicked.Add((arg) -> {
                paginator.PrevPage();
                RedrawPage();
            });
        }
        UIRoundBoxButton nextButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            nextButton.SetBgColor(colors.Positive);
            nextButton.SetWidth(180);
            nextButton.SetLabel("Next page");
            nextButton.GetTransform().SetLocalPosition(-180, menuPosY);
            nextButton.Clicked.Add((arg) -> {
                paginator.NextPage();
                RedrawPage();
            });
        }
    }
    
    public @Override boolean UpdateInput() { return false; }
    
    public @Override void OnPreShowView()
    {
        super.OnPreShowView();
        controller = null;
        usingItem = false;
        RebuildEntries();
        RedrawPage();
    }
    
    /**
     * Sets the controller that requested for item use.
     */
    public void SetController(GuiGameHumanController controller)
    {
        this.controller = controller;
    }
    
    /**
     * Requests use of specified item.
     */
    private void UseItem(ItemInfo item)
    {
        if(usingItem)
            return;
        if(controller != null)
        {
            // Notify controller.
            usingItem = true;
            controller.UseItem(item, () -> usingItem = false);
            // Decrease item from inventory.
            Entry entry = paginator.Find(e -> e.Item == item);
            if(entry != null)
            {
                entry.Count --;
                if(entry.Count <= 0)
                    paginator.Remove(entry);
                itemStore.RemoveItem(item);
                itemStore.Save();
            }
            
            // Display message
            UIQuickMessageOverlay message = overlays.GetView(UIQuickMessageOverlay.class);
            if(message != null)
                message.ShowMessage("Used item: " + item.Name);
        }
        
        int lastPage = paginator.GetCurPage();
        RebuildEntries();
        paginator.SetPage(lastPage);
        RedrawPage();
    }
    
    /**
     * Rebuilds the paginator.
     */
    private void RebuildEntries()
    {
        paginator.Clear();
        for(OwnedItemEntity item : itemStore.GetItems())
        {
            Entry entry = paginator.Find(e -> e.Item == item.GetInfo());
            if(entry == null)
                paginator.Add(entry = new Entry(item.GetInfo()));
            entry.Count ++;
        }
        paginator.Sort(null);
    }
    
    private void RedrawPage()
    {
        itemList.Clear();
        for(Entry entry : paginator.GetItemsInPage())
            itemList.ShowCell(entry.Item, entry.Count);
        itemList.PlayShowAni();
        
        pageLabel.SetText(String.format("%d / %d", paginator.GetCurPage(), paginator.GetMaxPage()));
    }
    
    /**
     * A single cell information to be displayed.
     */
    private class Entry implements Comparable<Entry> {
        
        public ItemInfo Item;
        public int Count;
        
        public Entry(ItemInfo item)
        {
            Item = item;
        }

        public @Override int compareTo(Entry other)
        {
            return Integer.compare(Item.Id, other.Item.Id);
        }
    }
}
