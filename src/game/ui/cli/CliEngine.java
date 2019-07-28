/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;

/**
 * Handles the rendering of text buffers on the console window.
 * @author jerrykim
 */
public class CliEngine {
    
    private CliBuffer buffer = new CliBuffer(100, 20);
    
    /**
     * Root displayer object.
     */
    private CliDisplayer root = new CliRoot();
    
    
    public CliEngine(IDependencyContainer container)
    {
        root.SetDependencyContainer(container);
    }
    
    /**
     * Adds specified displayer to the root object.
     */
    public void AddDisplay(CliDisplayer root)
    {
        this.root.AddChild(root);
    }
    
    /**
     * Renders the console view.
     */
    public void Render()
    {
        // Try rendering the screen
        root.Render(buffer);
        
        // If the screen has not changed, return.
        if(!buffer.IsDirty())
            return;
        
        // Set buffer to clean state
        buffer.SetDirty(false);
        
        // Output the buffer to console.
        System.out.println(buffer.GetRawBuffers());
    }
}
