/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class AnimeIT {

    
    /**
     * Testing initialization of anime.
     */
    @Test
    public void TestInit()
    {
        Anime anime = new Anime();
        assertEquals(anime.GetDuration(), 0);
        assertEquals(anime.GetTimeDuration(), 0, 0.000001);
        assertEquals(anime.GetCurrentFrame(), 0);
        assertFalse(anime.IsPlaying());
        
        anime = new Anime();
        anime.AddEvent(5, null);
        anime.AddSection(0, 1, null);
        assertEquals(anime.GetDuration(), 5);
        
        anime.AddSection(2, 10, null);
        assertEquals(anime.GetDuration(), 10);
        
        // Seeking within duration
        anime.SeekTo(6);
        assertEquals(anime.GetCurrentFrame(), 6);
        
        // Seeking past duration
        anime.SeekTo(15);
        assertEquals(anime.GetCurrentFrame(), 10);
    }
    
    /**
     * Testing general usage of anime.
     */
    @Test
    public void TestPlay()
    {
        // Set to 10 FPS
        Anime anime = new Anime(10);
        DummyObject dummy = new DummyObject();
        
        // 0 to 10 frames should be 1 second.
        anime.AddSection(0, 10, (progress) -> {
            dummy.Progress = progress;
        });
        anime.AddEvent(5, () -> {
            dummy.Callbacked = true;
        });
        
        // Play
        anime.Play();
        
        // Update 0.5 second worth of time.
        anime.Update(0.5f);
        assertEquals(dummy.Callbacked, true);
        assertEquals(dummy.Progress, 0.5, 0.000001);
       
        // Pause and update few seconds.
        anime.Pause();
        anime.Update(2f);
        assertEquals(dummy.Progress, 0.5, 0.000001);
        
        // Resume
        anime.Play();
        assertEquals(dummy.Progress, 0.5, 0.000001);
        
        // Update another 0.5 second
        anime.Update(0.5f);
        assertEquals(dummy.Progress, 1, 0.000001);
        
        // Stop update.
        anime.Stop();
        assertEquals(dummy.Progress, 0, 0.000001);
        
        // Play from half frames.
        anime.PlayAt(anime.GetDuration() / 2);
        assertEquals(dummy.Progress, 0.5, 0.000001);
        
        // Change stop mode to end.
        anime.SetStopMode(AnimationStop.End);
        anime.Stop();
        assertEquals(dummy.Progress, 1, 0.000001);
        
        // Change stop mode to none.
        anime.SeekTo(anime.GetDuration() / 2);
        anime.SetStopMode(AnimationStop.None);
        anime.Stop();
        assertEquals(dummy.Progress, 0.5, 0.000001);
        
        // Change wrap mode to loop.
        anime.SetWrapMode(AnimationWrap.Loop);
        anime.PlayAt(0);
        anime.Update(1.5f);
        assertEquals(dummy.Progress, 0.5, 0.00001);
        
        // Change wrap mode to end.
        anime.SetWrapMode(AnimationWrap.Reset);
        anime.Update(1.5f);
        assertEquals(dummy.Progress, 0, 0.00001);
        assertFalse(anime.IsPlaying());
    }
    
    private class DummyObject {
        public float Progress;
        public boolean Callbacked;
    }
}
