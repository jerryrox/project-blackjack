/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.data.Rect;
import game.debug.ILogger;
import game.graphics.ColorPreset;
import game.ui.gui.UIFrame;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.graphics.GuiFontProvider;
import game.ui.gui.graphics.UIAtlas;
import game.ui.gui.objects.UIScene;
import game.ui.gui.overlays.UIDialogOverlay;
import game.ui.gui.overlays.UIQuitOverlay;

/**
 * Game implementation for Gui application build.
 * @author jerrykim
 */
public class GuiGame extends BaseGame {
    
    public static final String Version = "0.0.1";
    
    private UIFrame frame;
    private GuiFontProvider fontProvider;
    private UIAtlas atlas;
    private ColorPreset colors;
    
    private UIScreenController screenController;
    private UIOverlayController overlayController;
    
    private boolean isQuitting = false;
    
    
    public GuiGame(GameArguments args)
    {
        super(args);
    }
    
    protected @Override void Initialize()
    {
        super.Initialize();
        
        dependencies.Cache(frame = new UIFrame());
        dependencies.Cache(fontProvider = new GuiFontProvider());
        dependencies.Cache(atlas = new UIAtlas());
        dependencies.Cache(colors = new ColorPreset());
        
        // Cache the root panel for internal UI rendering components.
        dependencies.Cache(frame.GetRootPanel());
        
        UIScene scene = frame.GetRootPanel().GetScene();
        dependencies.Cache(screenController = new UIScreenController(scene));
        dependencies.Cache(overlayController = new UIOverlayController(scene));
    }
    
    protected @Override void PostInitialize()
    {
        super.PostInitialize();
        
        // Import sprite images into atlas.
        atlas.AddSprite("action-draw-bg");
        atlas.AddSprite("action-skip-bg");
        atlas.AddSprite("box", new Rect(1, 1, 1, 1));
        atlas.AddSprite("card-back");
        atlas.AddSprite("card-frame", new Rect(17, 17, 2, 2));
        atlas.AddSprite("casual-icon");
        atlas.AddSprite("casual-title");
        atlas.AddSprite("glow");
        atlas.AddSprite("gradation_0", new Rect(0, 1, 116, 18));
        atlas.AddSprite("gradation_1", new Rect(0, 1, 117, 18));
        atlas.AddSprite("gradation_2", new Rect(0, 1, 233, 18));
        atlas.AddSprite("icon-arrow-left");
        atlas.AddSprite("icon-arrow-right");
        atlas.AddSprite("icon-coin");
        atlas.AddSprite("icon-cross");
        atlas.AddSprite("icon-damage");
        atlas.AddSprite("icon-heart");
        atlas.AddSprite("icon-menu");
        atlas.AddSprite("icon-pause");
        atlas.AddSprite("icon-play");
        atlas.AddSprite("icon-power");
        atlas.AddSprite("icon-retry");
        atlas.AddSprite("info-panel-bg");
        atlas.AddSprite("loading");
        atlas.AddSprite("logo-aut");
        atlas.AddSprite("mode-button-glow");
        atlas.AddSprite("null");
        atlas.AddSprite("pattern");
        atlas.AddSprite("playground");
        atlas.AddSprite("round-box", new Rect(8, 8, 10, 10));
        atlas.AddSprite("survival-icon");
        atlas.AddSprite("survival-title");
        atlas.AddSprite("title");
    }

    protected @Override void OnStart()
    {
        frame.Initialize(dependencies);
    }

    public @Override String GetVersion()  { return Version; }

    public @Override void ForceQuit()
    {
        System.exit(0);
    }

    public @Override void Quit()
    {
        if(isQuitting)
            return;
        
        UIDialogOverlay dialog = overlayController.ShowView(UIDialogOverlay.class);
        dialog.SetYesNoMode(
            "Are you sure you want to quit Project: Blackjack?",
            () -> {
                isQuitting = true;
                overlayController.ShowView(UIQuitOverlay.class);
            },
            null
        );
    }
}
