/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.overlays;

import game.allocation.InitWithDependency;
import game.data.Action;
import game.graphics.ColorPreset;
import game.ui.gui.UIOverlayController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIOverlay;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import java.awt.Color;

/**
 * Dialog message displayer overlay.
 * @author jerrykim
 */
public class UIDialogOverlay extends UIOverlay {
    
    private UILabel message;
    private UIRoundBoxButton yesButton;
    private UIRoundBoxButton noButton;
    
    private Action onYes;
    private Action onNo;
    
    
    public UIDialogOverlay()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors, UIOverlayController overlays)
    {
        uiObject.SetDepth(100000000);
        
        UISprite dark = uiObject.CreateChild().AddComponent(new UISprite());
        {
            dark.SetSpritename("box");
            dark.SetSize(1280, 720);
            dark.SetColor(Color.black);
            dark.SetAlpha(0.75f);
            dark.GetObject().SetDepth(-2);
        }
        UISprite bg = uiObject.CreateChild().AddComponent(new UISprite());
        {
            bg.SetSpritename("box");
            bg.SetSize(1280, 300);
            bg.SetColor(Color.white);
            bg.SetAlpha(0.125f);
            bg.GetObject().SetDepth(-1);
        }
        message = uiObject.CreateChild().AddComponent(new UILabel());
        {
            message.GetTransform().SetLocalPosition(0, -60);
        }
        uiObject.AddChild(yesButton = new UIRoundBoxButton());
        {
            yesButton.SetBgColor(colors.Positive);
            yesButton.SetLabel("Confirm");
            yesButton.Clicked.Add((arg) -> {
                if(onYes != null)
                    onYes.Invoke();
                overlays.HideView(this);
            });
        }
        uiObject.AddChild(noButton = new UIRoundBoxButton());
        {
            noButton.SetBgColor(colors.Negative);
            noButton.SetLabel("Cancel");
            noButton.Clicked.Add((arg) -> {
                if(onNo != null)
                    onNo.Invoke();
                overlays.HideView(this);
            });
        }
    }
    
    public @Override boolean UpdateInput() { return false; }
    
    protected @Override void OnPostHideView()
    {
        super.OnPostHideView();
        onYes = null;
        onNo = null;
    }
    
    /**
     * Setup display for Yes button only dialog.
     */
    public void SetYesMode(String message, Action onYes)
    {
        this.message.SetText(message);
        yesButton.GetTransform().SetLocalPosition(0, 70);
        noButton.SetActive(false);
        
        this.onYes = onYes;
    }
    
    /**
     * Setup disply for Yes/No buttons dialog.
     */
    public void SetYesNoMode(String message, Action onYes, Action onNo)
    {
        this.message.SetText(message);
        yesButton.GetTransform().SetLocalPosition(0, 40);
        noButton.GetTransform().SetLocalPosition(0, 100);
        noButton.SetActive(true);
        
        this.onYes = onYes;
        this.onNo = onNo;
    }
}
