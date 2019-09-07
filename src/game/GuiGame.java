/*
 * Jerry Kim (18015036), 2019
 */
package game;

import game.debug.ILogger;
import game.ui.gui.GuiEngine;
import game.ui.gui.IGuiEngine;
import game.ui.gui.graphics.GuiFontProvider;

/**
 * Game implementation for Gui application build.
 * @author jerrykim
 */
public class GuiGame extends BaseGame {
    
    private GuiEngine guiEngine;
    private GuiFontProvider fontProvider;
    
    
    public GuiGame(ILogger logger)
    {
        super(logger);
    }
    
    protected @Override void Initialize()
    {
        super.Initialize();
        
        dependencies.CacheAs(IGuiEngine.class, guiEngine = new GuiEngine(dependencies));
        
        dependencies.Cache(fontProvider = new GuiFontProvider());
    }
    
    protected @Override void PostInitialize()
    {
        super.PostInitialize();
    }

    protected @Override void OnStart()
    {
        guiEngine.Start();
    }
}
