/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import game.data.Action;
import game.data.ActionT;

/**
 * Anime controller interface.
 * @author jerrykim
 */
public interface IAnime {
    
    /**
     * Returns the duration of the entire animation in frames.
     */
    int GetDuration();
    
    /**
     * Returns the duration of the entire animation in seconds.
     */
    float GetTimeDuration();
    
    /**
     * Returns the current frame index.
     */
    int GetCurrentFrame();
    
    /**
     * Returns whether the animation is currently playing.
     */
    boolean IsPlaying();
    
    /**
     * Creates a new animation section. 
     */
    void AddSection(int from, int to, ActionT<Float> action);
    
    /**
     * Creates a new animation section.
     */
    void AddSection(int from, int to, EaseType easeType, ActionT<Float> action);
    
    /**
     * Creates a new event at specified frame.
     */
    void AddEvent(int frame, Action action);
    
    /**
     * Sets animation wrap mode on end.
     */
    void SetWrapMode(AnimationWrap mode);
    
    /**
     * Sets animation stop mode on Stop() call.
     */
    void SetStopMode(AnimationStop mode);
    
    /**
     * Plays animation at current frame.
     */
    void Play();
    
    /**
     * Starts animation playback from specified frame.
     */
    void PlayAt(int frame);
    
    /**
     * Pauses animation at current frame.
     */
    void Pause();
    
    /**
     * Stops animation with accordance to current animation stop mode.
     */
    void Stop();
    
    /**
     * Seeks time to specified frame.
     */
    void SeekTo(int frame);
    
    /**
     * Performs update for current game frame.
     */
    void Update(float deltaTime);
}
