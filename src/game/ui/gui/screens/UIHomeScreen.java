/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.screens;

import game.GuiGame;
import game.IGame;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.animations.Anime;
import game.animations.Easing;
import game.debug.Debug;
import game.entities.UserEntity;
import game.graphics.ColorPreset;
import game.ui.Pivot;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.components.ui.UILabel;
import game.ui.gui.components.ui.UIScreen;
import game.ui.gui.components.ui.UISprite;
import game.ui.gui.objects.customized.UIRoundBoxButton;
import game.ui.gui.overlays.UIDialogOverlay;
import game.ui.gui.overlays.UIRegisterOverlay;

/**
 * Home screen.
 * @author jerrykim
 */
public class UIHomeScreen extends UIScreen {
    
    private Anime onShownAni;
    
    private UIRoundBoxButton playButton;
    private UIRoundBoxButton quitButton;
    
    @ReceivesDependency
    private UIScreenController screens;
    
    @ReceivesDependency
    private UIOverlayController overlays;
    
    @ReceivesDependency
    private UserEntity user;
    
    @ReceivesDependency
    private IGame game;
    
    
    public UIHomeScreen()
    {
        super();
    }
    
    @InitWithDependency
    private void Init(ColorPreset colors)
    {
        UISprite title = uiObject.CreateChild().AddComponent(new UISprite());
        {
            title.GetTransform().SetLocalPosition(0, -40);
            title.SetSpritename("title");
            title.ResetSize();
        }
        UILabel creator = uiObject.CreateChild().AddComponent(new UILabel());
        {
            creator.SetPivot(Pivot.BottomLeft);
            creator.GetTransform().SetLocalPosition(-620f, 350);
            creator.SetText("Jerry Kim (18015036), 2019");
        }
        UILabel version = uiObject.CreateChild().AddComponent(new UILabel());
        {
            version.SetPivot(Pivot.BottomRight);
            version.GetTransform().SetLocalPosition(620, 350);
            version.SetText("Version " + GuiGame.Version);
        }
        uiObject.AddChild(playButton = new UIRoundBoxButton());
        {
            playButton.SetBgColor(colors.Neutral);
            playButton.SetLabel("Play");
            playButton.GetTransform().SetLocalPosition(0, 100);
            playButton.Clicked.Add((arg) -> {
                OnPlayButton();
            });
        }
        uiObject.AddChild(quitButton = new UIRoundBoxButton());
        {
            quitButton.SetBgColor(colors.Negative);
            quitButton.SetLabel("Quit");
            quitButton.GetTransform().SetLocalPosition(0, 170);
            quitButton.Clicked.Add((arg) -> {
                OnQuitButton();
            });
        }
        
        onShownAni = animator.CreateAnime("onshown");
        {
            onShownAni.AddEvent(0, () -> {
                title.SetAlpha(0);
            });
            onShownAni.AddSection(12, 48, (progress) -> {
                title.SetAlpha(Easing.QuadEaseOut(progress, 0, 1f, 0));
                
                float scale = Easing.BackEaseOut(progress, 0.75f, 0.25f, 0.6f);
                title.GetTransform().SetLocalScale(scale, scale);
            });
        }
    }
    
    public @Override void OnActive()
    {
        super.OnActive();
        
        onShownAni.PlayAt(0);
    }
    
    public @Override void OnInactive()
    {
        super.OnInactive();
        
        onShownAni.Stop();
    }
    
    private void OnPlayButton()
    {
        if(user.IsEmptyData())
            overlays.ShowView(UIRegisterOverlay.class);
        else
            screens.ShowView(UIMainScreen.class);
    }
    
    private void OnQuitButton()
    {
        game.Quit();
    }
}
