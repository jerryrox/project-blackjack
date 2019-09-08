/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.gui.components;

import game.allocation.ReceivesDependency;
import game.animations.Anime;
import game.debug.Debug;
import game.debug.ILogger;
import java.util.HashMap;

/**
 * Holds and processes anime objects passed to this object.
 * @author jerrykim
 */
public class UIAnimator extends UIComponent {
    
    /**
     * Table of animations mapped to animation names.
     */
    private HashMap<String, Anime> animations = new HashMap<>();
    
    @ReceivesDependency
    private ILogger logger;
    
    
    public UIAnimator()
    {
    }
    
    /**
     * Creates a new Anime instance for specified name and returns it.
     * Returns null if the name is already taken.
     */
    public Anime CreateAnime(String name)
    {
        if(animations.containsKey(name))
        {
            logger.LogWarning("UIAnimator.CreateAnime - An animation already exists with name: " + name);
            return null;
        }
        Anime anime = new Anime();
        animations.put(name, anime);
        return anime;
    }
    
    /**
     * Returns the anime instance associated with specified name.
     * @param name
     * @return 
     */
    public Anime GetAnime(String name) { return animations.get(name); }
    
    /**
     * Removes anime instance associated with specified name.
     */
    public void RemoveAnime(String name) { animations.remove(name); }
    
    /**
     * Stops all anime instances stored in this animator.
     */
    public void StopAll()
    {
        for(Anime anime : animations.values())
            anime.Stop();
    }
    
    public @Override void Update(float deltaTime)
    {
        super.Update(deltaTime);
        
        for(Anime anime : animations.values())
            anime.Update(deltaTime);
    }
}
