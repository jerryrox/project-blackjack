/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;
import game.allocation.ReceivesDependency;
import game.data.Vector2;
import game.ui.IDisplayer;
import game.ui.cli.commands.CommandContext;
import java.util.TreeSet;

/**
 * Displayer implementation for CLI UI engine.
 * @author jerrykim
 */
public class CliDisplayer<T extends CliDisplayer> implements IDisplayer<T>, Comparable<CliDisplayer> {
    
    /**
     * List of children nested under this displayer, sorted by their depth values.
     */
    protected TreeSet<T> children = new TreeSet<T>();
    
    /**
     * Whether the displayer is currently active.
     */
    protected boolean isActive = true;
    
    /**
     * Command holder for this displayer.
     */
    protected CommandContext commands = new CommandContext();
    
    /**
     * Position of the displayer.
     */
    protected Vector2 position = new Vector2();
    
    /**
     * Dependency container instance for automated injection of dependencies on all child displayers.
     */
    @ReceivesDependency
    private IDependencyContainer dependencyContainer;
    
    
    public @Override int GetDepth() { return 0; }
    
    public @Override void SetActive(boolean isActive)
    {
        this.isActive = isActive;
        if(isActive)
            OnEnable();
        else
            OnDisable();
    }
    
    public @Override void OnEnable() {}
    
    public @Override void OnDisable() {}
    
    public @Override boolean IsActive() { return isActive; }
    
    public @Override void SetPosition(int x, int y) { position.X = x; position.Y = y; }
    
    public @Override void SetPositionX(int x) { position.X = x; }
    
    public @Override void SetPositionY(int y) { position.Y = y; }
    
    public @Override Vector2 GetPosition() { return position.Clone(); }
    
    public @Override <TDisplayer extends T> TDisplayer AddChild(TDisplayer child)
    {
        if(dependencyContainer != null)
            dependencyContainer.Inject(child);
        children.add(child);
        return child;
    }
    
    public @Override void AddChildren(T... children)
    {
        for(int i=0; i<children.length; i++)
        {
            if(dependencyContainer != null)
                dependencyContainer.Inject(children[i]);
            this.children.add(children[i]);
        }
    }
    
    public @Override void RemoveChild(T child)
    {
        children.remove(child);
    }
    
    public @Override Iterable<T> GetChildren(boolean reverse)
    {
        if(!reverse)
            return children;
        return children.descendingSet();
    }
    
    public @Override int compareTo(CliDisplayer displayer)
    {
        int c = Integer.compare(GetDepth(), displayer.GetDepth());
        if(c == 0)
            return Integer.compare(hashCode(), displayer.hashCode());
        return c;
    }
    
    /**
     * Performs rendering of text on specified buffer object.
     * Any overridden implementation should call this method after rendering their own stuffs.
     */
    public void Render(CliBuffer buffer)
    {
        for(CliDisplayer displayer : children)
        {
            if(displayer.IsActive())
                displayer.Render(buffer);
        }
    }
    
    /**
     * Returns the command context being managed by this displayer.
     */
    public CommandContext GetCommands() { return commands; }
}
