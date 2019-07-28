/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.IDependencyContainer;
import game.ui.IDisplayer;
import java.util.TreeSet;

/**
 * Displayer implementation for CLI UI engine.
 * @author jerrykim
 */
public class CliDisplayer implements IDisplayer<CliDisplayer> {
    
    /**
     * List of children nested under this displayer, sorted by their depth values.
     */
    protected TreeSet<CliDisplayer> children = new TreeSet<CliDisplayer>();
    
    /**
     * Dependency container instance for automated injection of dependencies on all child displayers.
     */
    private IDependencyContainer dependencyContainer;
    
    
    public @Override int GetDepth() { return 0; }
    
    public @Override void AddChild(CliDisplayer child)
    {
        if(dependencyContainer != null)
            dependencyContainer.Inject(child);
        children.add(child);
    }
    
    public @Override void AddChildren(CliDisplayer... children)
    {
        for(int i=0; i<children.length; i++)
        {
            if(dependencyContainer != null)
                dependencyContainer.Inject(children[i]);
            this.children.add(children[i]);
        }
    }
    
    public @Override void RemoveChild(CliDisplayer child)
    {
        children.remove(child);
    }
    
    public @Override void SetDependencyContainer(IDependencyContainer container) { dependencyContainer = container; }
    
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
            displayer.Render(buffer);
    }
}
