/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import game.data.Action;

/**
 * Specialized anime section for events.
 * @author jerrykim
 */
public class AnimeEventSection extends AnimeSection {
    
    private Action callback;
    private Anime anime;
    
    
    public AnimeEventSection(Anime anime, int frame, Action callback)
    {
        super(0, frame, frame, EaseType.Linear, null);
        this.callback = callback;
        this.anime = anime;
    }
    
    protected @Override void ActivateState()
    {
        if(!anime.IsPlaying())
            return;
        if(curFrame >= from && lastFrame < from && callback != null)
            callback.Invoke();
    }
}
