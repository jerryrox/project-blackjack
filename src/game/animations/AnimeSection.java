/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import game.data.ActionT;

/**
 * A single time section in an animation.
 * @author jerrykim
 */
public class AnimeSection {
    
    protected boolean isActive;
    protected int curFrame;
    protected int lastFrame;
    
    protected float frameTime;
    protected int duration;
    protected int from;
    protected int to;
    protected EaseType easeType;
    
    protected ActionT<Float> action;
    
    
    public AnimeSection(float frameTime, int from, int to, EaseType easeType, ActionT<Float> action)
    {
        this.frameTime = frameTime;
        this.duration = to - from;
        this.from = from;
        this.to = to;
        this.easeType = easeType;
        this.action = action;
    }
    
    /**
     * Returns section start frame.
     */
    public int GetFrom() { return from; }
    
    /**
     * Returns section end frame.
     */
    public int GetTo() { return to; }
    
    /**
     * Returns duration between end and start frames.
     */
    public int GetDuration() { return duration; }
    
    /**
     * Seeks to specified frame.
     * @param frame 
     */
    public void SeekState(int frame)
    {
        lastFrame = curFrame = frame - 1;
        isActive = true;
        UpdateState(frame);
    }
    
    /**
     * Updates and possibly activates callback action for current state.
     */
    public void UpdateState(int frame)
    {
        if(!isActive)
            return;
        if(frame < from || (frame > to && lastFrame >= to))
            return;
        
        lastFrame = curFrame;
        curFrame = frame;
        
        ActivateState();
        isActive = ShouldBeActive();
    }
    
    /**
     * Invokes callback action for current frame.
     */
    protected void ActivateState()
    {
        if(action == null)
            return;
        action.Invoke(Easing.Ease(easeType, GetProgress(), 0, 1, duration * frameTime));
    }
    
    /**
     * Returns the update progress on current frame.
     */
    private float GetProgress()
    {
        float progress = ((float)curFrame - (float)from) / ((float)to - (float)from);
        if(progress < 0)
            return 0;
        else if(progress > 1)
            return 1;
        return progress;
    }
    
    /**
     * Returns whether section should be active on current frame.
     */
    private boolean ShouldBeActive() { return curFrame >= from && curFrame <= to; }
}
