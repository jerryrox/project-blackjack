/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui;

import game.allocation.IDependencyContainer;

/**
 * Governs the overall GUI elements in the application.
 * @author jerrykim
 */
public class GuiEngine implements IGuiEngine {
    
    private UIFrame frame;
    
    private IDependencyContainer dependencies;
    
    
    public GuiEngine(IDependencyContainer dependencies)
    {
        this.dependencies = dependencies;
        
        frame = new UIFrame();
        dependencies.Cache(frame);
    }
    
    public void Start()
    {
        frame.Initialize(dependencies);
    }
}
