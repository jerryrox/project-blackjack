/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.data.Rect;
import game.debug.ILogger;
import game.ui.gui.GuiEngine;
import game.ui.gui.IGuiEngine;
import game.ui.gui.UIOverlayController;
import game.ui.gui.UIScreenController;
import game.ui.gui.graphics.GuiFontProvider;
import game.ui.gui.graphics.UIAtlas;
import game.ui.gui.objects.UIScene;

/**
 * Game implementation for Gui application build.
 * @author jerrykim
 */
public class GuiGame extends BaseGame {
    
    private GuiEngine guiEngine;
    private GuiFontProvider fontProvider;
    private UIAtlas atlas;
    
    private UIScreenController screenController;
    private UIOverlayController overlayController;
    
    
    public GuiGame(ILogger logger)
    {
        super(logger);
    }
    
    protected @Override void Initialize()
    {
        super.Initialize();
        
        dependencies.CacheAs(IGuiEngine.class, guiEngine = new GuiEngine(dependencies));
        dependencies.Cache(fontProvider = new GuiFontProvider());
        dependencies.Cache(atlas = new UIAtlas());
        
        UIScene scene = guiEngine.GetFrame().GetRootPanel().GetScene();
        dependencies.Cache(screenController = new UIScreenController(scene));
        dependencies.Cache(overlayController = new UIOverlayController(scene));
    }
    
    protected @Override void PostInitialize()
    {
        super.PostInitialize();
        
        // Import sprite images into atlas.
        atlas.AddSprite("box", new Rect(1, 1, 1, 1));
        atlas.AddSprite("glow");
        atlas.AddSprite("gradation_0", new Rect(0, 1, 116, 18));
        atlas.AddSprite("gradation_1", new Rect(0, 1, 117, 18));
        atlas.AddSprite("gradation_2", new Rect(0, 1, 233, 18));
        atlas.AddSprite("icon-arrow-left");
        atlas.AddSprite("icon-arrow-right");
        atlas.AddSprite("icon-menu");
        atlas.AddSprite("icon-pause");
        atlas.AddSprite("icon-play");
        atlas.AddSprite("icon-power");
        atlas.AddSprite("icon-retry");
        atlas.AddSprite("loading");
        atlas.AddSprite("logo-aut");
        atlas.AddSprite("null");
        atlas.AddSprite("playground");
        atlas.AddSprite("round-box", new Rect(8, 8, 10, 10));
        atlas.AddSprite("title");
    }

    protected @Override void OnStart()
    {
        guiEngine.Start();
    }
}
