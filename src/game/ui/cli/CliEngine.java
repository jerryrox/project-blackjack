/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;

/**
 * Handles the rendering of text buffers on the console window.
 * @author jerrykim
 */
public class CliEngine implements ICliEngine {
    
    /**
     * The buffer object to display the ui displayers.
     */
    private CliBuffer buffer = new CliBuffer(100, 20);
    
    /**
     * Root displayer object.
     */
    private CliRoot root;
    
    /**
     * Input manager for the engine.
     */
    private InputHandler inputHandler = new InputHandler();
    
    /**
     * Whether the engine is currently running.
     */
    private boolean isRunning;
    
    
    public CliEngine(CliRoot root, IDependencyContainer container)
    {
        this.root = root;
        
        // Set input propagation root point.
        inputHandler.SetRoot(root);
    }
    
    /**
     * Returns the root displayer object which can be added with child displayers to display
     * their stuffs on the console.
     */
    public CliRoot GetRootDisplay() { return root; }
    
    /**
     * Returns the input management instance of the engine.
     */
    public InputHandler GetInputHandler() { return inputHandler; }
    
    /**
     * Starts the engine, rendering the displayers every after user input.
     */
    public @Override void StartUpdate()
    {
        isRunning = true;
        while(isRunning)
        {
            // Try rendering the screen
            root.Render(buffer);

            // If the screen has not changed, return.
            if(!buffer.IsDirty())
                return;

            // Set buffer to clean state
            buffer.SetDirty(false);

            // Render padding
            System.out.print(buffer.PaddingBuffer);

            // Render commands
            inputHandler.PromptCommands();

            // Output the buffer to console.
            System.out.print(buffer.GetRawBuffers());

            // Listen to user interaction through command.
            inputHandler.ListenToCommand();
        }
    }
    
    /**
     * Stops running the engine.
     */
    public @Override void StopUpdate()
    {
        isRunning = false;
    }
}
