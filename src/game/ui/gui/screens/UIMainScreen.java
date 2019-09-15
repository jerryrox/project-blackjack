/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.IGame;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.entities.UserEntity;
import game.entities.UserStats;
import game.graphics.ColorPreset;
import game.rulesets.Card;
import game.ui.gui.UIInput;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.objects.customized.UICardDisplayer;
import game.ui.gui.objects.customized.UIMainStatDisplayer;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import game.ui.gui.overlays.UIDialogOverlay;
import game.ui.gui.overlays.UITopMenuOverlay;
import game.utils.Random;
import java.awt.event.KeyEvent;

/**
 * Main menu screen.
 * @author jerrykim
 */
public class UIMainScreen extends UIScreen {
    
    private UICardDisplayer card;
    
    private UIMainStatDisplayer powerDisplayer;
    private UIMainStatDisplayer armorDisplayer;
    private UIMainStatDisplayer enduranceDisplayer;
    private UIMainStatDisplayer luckDisplayer;
    private UIMainStatDisplayer fortuneDisplayer;
    
    private UIRoundBoxButton playButton;
    private UIRoundBoxButton shopButton;
    private UIRoundBoxButton inventoryButton;
    private UIRoundBoxButton helpButton;
    private UIRoundBoxButton quitButton;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private IGame game;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private UIInput input;
    
    
    public UIMainScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        uiObject.AddChild(card = new UICardDisplayer());
        {
            card.GetTransform().SetLocalPosition(0, -10);
            ShowRandomCard(false);
        }
        UILabel changeLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            changeLabel.SetText("Press spacebar to change");
            changeLabel.SetFontSize(16);
            changeLabel.GetTransform().SetLocalPosition(0, 210);
        }
        
        final float statPosX = -440;
        UILabel statsLabel = uiObject.CreateChild().AddComponent(new UILabel());
        {
            statsLabel.SetText("Your stats");
            statsLabel.GetTransform().SetLocalPosition(statPosX, -210);
        }
        uiObject.AddChild(powerDisplayer = new UIMainStatDisplayer());
        {
            powerDisplayer.GetTransform().SetLocalPosition(statPosX, -160);
        }
        uiObject.AddChild(armorDisplayer = new UIMainStatDisplayer());
        {
            armorDisplayer.GetTransform().SetLocalPosition(statPosX, -85);
        }
        uiObject.AddChild(enduranceDisplayer = new UIMainStatDisplayer());
        {
            enduranceDisplayer.GetTransform().SetLocalPosition(statPosX, -10);
        }
        uiObject.AddChild(luckDisplayer = new UIMainStatDisplayer());
        {
            luckDisplayer.GetTransform().SetLocalPosition(statPosX, 65);
        }
        uiObject.AddChild(fortuneDisplayer = new UIMainStatDisplayer());
        {
            fortuneDisplayer.GetTransform().SetLocalPosition(statPosX, 140);
        }
        
        final int buttonWidth = 160;
        final int buttonPosY = 310;
        uiObject.AddChild(playButton = new UIRoundBoxButton());
        {
            playButton.SetBgColor(colors.Neutral);
            playButton.SetLabel("Play");
            playButton.SetWidth(buttonWidth);
            playButton.GetTransform().SetLocalPosition(-400, buttonPosY);
            playButton.Clicked.Add((arg) -> { OnPlayButton(); });
        }
        uiObject.AddChild(shopButton = new UIRoundBoxButton());
        {
            shopButton.SetBgColor(colors.Positive);
            shopButton.SetLabel("Shop");
            shopButton.SetWidth(buttonWidth);
            shopButton.GetTransform().SetLocalPosition(-200, buttonPosY);
            shopButton.Clicked.Add((arg) -> { OnShopButton(); });
        }
        uiObject.AddChild(inventoryButton = new UIRoundBoxButton());
        {
            inventoryButton.SetBgColor(colors.Positive);
            inventoryButton.SetLabel("Inventory");
            inventoryButton.SetWidth(buttonWidth);
            inventoryButton.GetTransform().SetLocalPosition(0, buttonPosY);
            inventoryButton.Clicked.Add((arg) -> { OnInventoryButton(); });
        }
        uiObject.AddChild(helpButton = new UIRoundBoxButton());
        {
            helpButton.SetBgColor(colors.Warning);
            helpButton.SetTextColor(colors.Dark);
            helpButton.SetLabel("Help");
            helpButton.SetWidth(buttonWidth);
            helpButton.GetTransform().SetLocalPosition(200, buttonPosY);
            helpButton.Clicked.Add((arg) -> { OnHelpButton(); });
        }
        uiObject.AddChild(quitButton = new UIRoundBoxButton());
        {
            quitButton.SetBgColor(colors.Negative);
            quitButton.SetLabel("Quit");
            quitButton.SetWidth(buttonWidth);
            quitButton.GetTransform().SetLocalPosition(400, buttonPosY);
            quitButton.Clicked.Add((arg) -> { OnQuitButton(); });
        }
    }
    
    public @Override boolean UpdateInput()
    {
        super.UpdateInput();
        if(input.IsKeyDown(KeyEvent.VK_SPACE))
        {
            ShowRandomCard(true);
        }
        return false;
    }
    
    protected @Override void OnPreShowView()
    {
        super.OnPreShowView();
        if(!overlays.IsActive(UITopMenuOverlay.class))
            overlays.ShowView(UITopMenuOverlay.class);
        
        RefreshStatDisplayers();
    }
    
    protected @Override void OnPostShowView()
    {
        super.OnPostShowView();
        card.SetSide(true, true);
    }
    
    /**
     * Refreshes all stat displayers.
     */
    private void RefreshStatDisplayers()
    {
        UserStats stats = user.GetStats();
        powerDisplayer.Setup(stats.Power);
        armorDisplayer.Setup(stats.Armor);
        enduranceDisplayer.Setup(stats.Endurance);
        luckDisplayer.Setup(stats.Luck);
        fortuneDisplayer.Setup(stats.Fortune);
    }
    
    /**
     * Shows a random card number on the card displayer.
     */
    private void ShowRandomCard(boolean animate)
    {
        if(card.IsAnimating())
            return;
        if(animate)
        {
            card.SetAnimationCallbacks(
                () -> { // Front
                    card.SetAnimationCallbacks(null, null);
                },
                () -> { // Back
                    card.SetCard(new Card());
                    card.SetSide(true, true);
                }
            );
            card.SetSide(false, true);
        }
        else
        {
            card.SetCard(new Card());
        }
    }
    
    private void OnPlayButton()
    {
        screens.ShowView(UIModeScreen.class);
    }
    
    private void OnShopButton()
    {
        screens.ShowView(UIShopScreen.class);
    }
    
    private void OnInventoryButton()
    {
        screens.ShowView(UIInventoryScreen.class);
    }
    
    private void OnStatButton()
    {
        
    }
    
    private void OnHelpButton()
    {
        screens.ShowView(UIHelpScreen.class);
    }
    
    private void OnQuitButton()
    {
        game.Quit();
    }
}
