/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.graphics.ColorPreset;
import game.io.store.UserStore;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.UITextBox;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import game.ui.gui.screens.UIMainScreen;
import java.awt.Color;

/**
 * Overlay for receiving username on first play.
 * @author jerrykim
 */
public class UIRegisterOverlay extends UIOverlay {
    
    private UITextBox usernameBox;
    private UIRoundBoxButton confirmButton;
    private UIRoundBoxButton cancelButton;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private UserStore userStore;
    
    
    public UIRegisterOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite dark = uiObject.CreateChild().AddComponent(new UISprite());
        {
            dark.SetSpritename("box");
            dark.SetSize(1280, 720);
            dark.SetColor(Color.black);
            dark.SetAlpha(0.75f);
            dark.GetObject().SetDepth(-1);
        }
        UILabel message = uiObject.CreateChild().AddComponent(new UILabel());
        {
            message.SetText("You don't have a profile yet.");
            message.GetTransform().SetLocalPosition(0, -60);
        }
        uiObject.AddChild(usernameBox = new UITextBox());
        {
            usernameBox.SetWidth(300);
            usernameBox.SetBgColor(colors.Lighter);
            usernameBox.SetTextColor(colors.Darker);
            usernameBox.SetMaxCharacters(16);
            usernameBox.SetRestriction(UITextBox.AlphanumericRestriction);
            usernameBox.GetTransform().SetLocalPosition(0, 0);
        }
        uiObject.AddChild(confirmButton = new UIRoundBoxButton());
        {
            confirmButton.SetLabel("Confirm");
            confirmButton.SetWidth(200);
            confirmButton.SetBgColor(colors.Positive);
            confirmButton.GetTransform().SetLocalPosition(-120f, 80);
            confirmButton.Clicked.Add((arg) -> {
                OnConfirmButton();
            });
        }
        uiObject.AddChild(cancelButton = new UIRoundBoxButton());
        {
            cancelButton.SetLabel("Cancel");
            cancelButton.SetWidth(200);
            cancelButton.SetBgColor(colors.Negative);
            cancelButton.GetTransform().SetLocalPosition(120f, 80);
            cancelButton.Clicked.Add((arg) -> {
                OnCancelButton();
            });
        }
    }
    
    public @Override boolean UpdateInput() { return false; }
    
    protected @Override void OnPreShowView()
    {
        super.OnPreShowView();
        usernameBox.SetValue(null);
    }
    
    private void OnConfirmButton()
    {
        String value = usernameBox.GetValue().trim();
        if(value.length() == 0)
            return;
        
        userStore.GetUser().Username.SetValue(value);
        userStore.Save();
        
        screens.ShowView(UIMainScreen.class);
        overlays.HideView(this);
    }
    
    private void OnCancelButton()
    {
        overlays.HideView(this);
    }
}
