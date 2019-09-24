/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import game.data.Action;
import game.data.ActionT;
import game.debug.Debug;
import java.util.ArrayList;

/**
 * Representation of a single animation.
 * @author jerrykim
 */
public class Anime implements IAnime {
    
    private float frameTime = 0;
    private float currentTime = 0;
    private int duration = 0;
    private int currentFrame = 0;
    private int lastFrame = 0;
    private boolean isPlaying = false;
    private AnimationWrap wrapMode = AnimationWrap.None;
    private AnimationStop stopMode = AnimationStop.Reset;
    private ArrayList<AnimeSection> sections = new ArrayList<>();
    
    
    public Anime() { this(60); }
    
    public Anime(int fps)
    {
        frameTime = 1f / fps;
    }
    
    public @Override int GetDuration() { return duration; }
    
    public @Override float GetTimeDuration() { return duration * frameTime; }
    
    public @Override int GetCurrentFrame() { return lastFrame; }
    
    public @Override boolean IsPlaying() { return isPlaying; }
    
    public @Override void AddSection(int from, int to, ActionT<Float> action) { AddSection(from, to, EaseType.Linear, action); }
    
    public @Override void AddSection(int from, int to, EaseType easeType, ActionT<Float> action)
    {
        if(from < 0 || to < 0 || to <= from)
            return;
        
        AddSection(new AnimeSection(frameTime, from, to, easeType, action));
        if(to > duration)
            duration = to;
    }
    
    public @Override void AddEvent(int frame, Action action)
    {
        if(frame < 0)
            return;
        
        AddSection(new AnimeEventSection(this, frame, action));
        if(frame > duration)
            duration = frame;
    }
    
    public @Override void SetWrapMode(AnimationWrap mode) { wrapMode = mode; }
    
    public @Override void SetStopMode(AnimationStop mode) { stopMode = mode; }
    
    public @Override void Play()
    {
        if(duration > 0 && !isPlaying)
        {
            isPlaying = true;
            SeekState(OverrideFrame(currentFrame));
        }
    }
    
    public @Override void PlayAt(int frame)
    {
        SeekTo(frame);
        Play();
    }
    
    public @Override void Pause()
    {
        if(duration > 0 && isPlaying)
        {
            isPlaying = false;
            UpdateState(OverrideFrame(currentFrame));
        }
    }
    
    public @Override void Stop()
    {
        if(duration > 0)
        {
            isPlaying = false;
            switch(stopMode)
            {
            case None: SeekState(OverrideFrame(currentFrame)); break;
            case Reset: SeekState(OverrideFrame(0)); break;
            case End: SeekState(OverrideFrame(duration)); break;
            }
        }
    }
    
    public @Override void SeekTo(int frame) { SeekState(OverrideFrame(frame)); }
    
    public @Override void Update(float deltaTime)
    {
        if(!isPlaying)
            return;
        
        // Perform progression
        currentTime += deltaTime;
        currentFrame = (int)(currentTime / frameTime);
        
        // If frame update
	if (currentFrame != lastFrame)
	{
            // End of frame
            if (currentFrame >= duration)
            {
                // Flush events
                UpdateState(duration);
                
                // Wrap animation
                switch (wrapMode)
                {
                case None: Pause(); return;
                case Reset: Pause(); SeekTo(0); return;
                case Loop:
                    SeekState(0);
                    lastFrame = 0;
                    float timeDuration = GetTimeDuration();
                    while (currentTime > timeDuration)
                        currentTime -= timeDuration;
                    currentFrame %= duration;
                    break;
                }
            }

            // Store frame
            lastFrame = currentFrame;

            // Update state
            UpdateState(currentFrame);
	}
    }
    
    /**
     * Overrides frame states using specified frame number and returns that same frame.
     */
    private int OverrideFrame(int frame)
    {
        if(frame < 0)
            frame = 0;
        else if(frame > duration)
            frame = duration;
        
        lastFrame = currentFrame = frame;
        currentTime = frame * frameTime;
        return frame;
    }
    
    /**
     * Seeks through the correct state in using specified frame.
     */
    private void SeekState(int frame)
    {
        for(int i=0; i<sections.size(); i++)
            sections.get(i).SeekState(frame);
    }
    
    /**
     * Updates states for current frame.
     */
    private void UpdateState(int frame)
    {
        for(int i=0; i<sections.size(); i++)
            sections.get(i).UpdateState(frame);
    }
    
    /**
     * Internally adds specified section.
     */
    private void AddSection(AnimeSection section)
    {
        sections.add(section);
        sections.sort((x, y) -> Integer.compare(x.GetFrom(), y.GetFrom()));
    }
}
