/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Paginator;
import game.entities.OwnedItemEntity;
import game.graphics.ColorPreset;
import game.io.store.ItemStore;
import game.rulesets.items.ItemInfo;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.customized.UIInventoryItem;
import game.ui.gui.objects.customized.UIItemList;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import java.awt.Font;

/**
 * Inventory displayer screen.
 * @author jerrykim
 */
public class UIInventoryScreen extends UIScreen {
    
    private UIItemList<UIInventoryItem> itemList;
    private Paginator<Entry> paginator;
    
    private UILabel pageLabel;
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    
    public UIInventoryScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors, UIScreenController screens)
    {
        UILabel title = uiObject.CreateChild().AddComponent(new UILabel());
        {
            title.SetText("Inventory");
            title.GetTransform().SetLocalPosition(0, -260f);
            title.SetFont(Font.BOLD, 24);
        }
        
        final int itemsPerPage = 8;
        paginator = new Paginator<>(itemsPerPage);
        uiObject.AddChild(itemList = new UIItemList<>(itemsPerPage, () -> new UIInventoryItem()));
        {
            itemList.GetTransform().SetLocalPosition(0, -180);
        }
        
        final float menuPosY = 320;
        UIRoundBoxButton shopButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            shopButton.SetBgColor(colors.Neutral);
            shopButton.SetWidth(180);
            shopButton.SetLabel("Shop");
            shopButton.GetTransform().SetLocalPosition(230, menuPosY);
            shopButton.Clicked.Add((arg) -> {
                screens.ShowView(UIShopScreen.class);
            });
        }
        UIRoundBoxButton mainButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            mainButton.SetBgColor(colors.Warning);
            mainButton.SetWidth(180);
            mainButton.SetLabel("Main menu");
            mainButton.SetTextColor(colors.Dark);
            mainButton.GetTransform().SetLocalPosition(480, menuPosY);
            mainButton.Clicked.Add((arg) -> {
                screens.ShowView(UIMainScreen.class);
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
    
    public @Override void OnPreShowView()
    {
        super.OnPreShowView();
        RebuildEntries();
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
