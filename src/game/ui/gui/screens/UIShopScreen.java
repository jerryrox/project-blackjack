/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.data.Paginator;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.io.store.ItemStore;
import game.io.store.UserStore;
import game.rulesets.items.ItemDefinitions;
import game.rulesets.items.ItemInfo;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.customized.UIInventoryItem;
import game.ui.gui.objects.customized.UIItemList;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import game.ui.gui.objects.customized.UIShopItem;
import game.ui.gui.overlays.DialogPresetter;
import java.awt.Font;

/**
 * Item shop screen.
 * @author jerrykim
 */
public class UIShopScreen extends UIScreen {
    
    private UIItemList<UIShopItem> itemList;
    private Paginator<ItemInfo> paginator;
    
    private UILabel pageLabel;
    
    @ReceivesDependency
    private ItemStore itemStore;
    
    @ReceivesDependency
    private UserStore userStore;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private ItemDefinitions items;
    
    
    public UIShopScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors, UIScreenController screens)
    {
        UILabel title = uiObject.CreateChild().AddComponent(new UILabel());
        {
            title.SetText("Shop");
            title.GetTransform().SetLocalPosition(0, -260f);
            title.SetFont(Font.BOLD, 24);
        }
        
        final int itemsPerPage = 8;
        paginator = new Paginator<>(itemsPerPage);
        uiObject.AddChild(itemList = new UIItemList<>(itemsPerPage, () -> new UIShopItem()));
        {
            itemList.GetTransform().SetLocalPosition(0, -180);
            itemList.SetCallback((item) -> {
                PurchaseItem(item);
            });
        }
        
        final float menuPosY = 320;
        UIRoundBoxButton shopButton = uiObject.AddChild(new UIRoundBoxButton());
        {
            shopButton.SetBgColor(colors.Neutral);
            shopButton.SetWidth(180);
            shopButton.SetLabel("Inventory");
            shopButton.GetTransform().SetLocalPosition(230, menuPosY);
            shopButton.Clicked.Add((arg) -> {
                screens.ShowView(UIInventoryScreen.class);
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
     * Purchases the specified item.
     */
    private void PurchaseItem(ItemInfo item)
    {
        // If not enough gold, return.
        if(user.Gold.GetValue() < item.BuyCost)
        {
            DialogPresetter.NotEnoughGold(overlays, null);
            return;
        }
        
        // Confirm whether to purchase this item.
        DialogPresetter.ConfirmBuyItem(overlays, item,
            () -> {
                // Add item and deduct gold.
                itemStore.AddItem(item);
                user.Gold.SetValue(user.Gold.GetValue() - item.BuyCost);
                // Save data
                itemStore.Save();
                userStore.Save();
            },
            null
        );
    }
    
    /**
     * Rebuilds the paginator.
     */
    private void RebuildEntries()
    {
        paginator.Clear();
        for(ItemInfo item : items.GetAllInfos())
            paginator.Add(item);
        paginator.Sort(null);
    }
    
    private void RedrawPage()
    {
        itemList.Clear();
        for(ItemInfo item : paginator.GetItemsInPage())
            itemList.ShowCell(item, item.BuyCost);
        itemList.PlayShowAni();
        
        pageLabel.SetText(String.format("%d / %d", paginator.GetCurPage(), paginator.GetMaxPage()));
    }
}
