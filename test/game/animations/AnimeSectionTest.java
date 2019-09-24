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
public class AnimeSectionTest {
    
    // 1 FPS
    private final float FrameTime = 1f;
    
    
    /**
     * Testing initialization of a new anime section and testing some getters.
     */
    @Test
    public void TestInit()
    {
        AnimeSection section = new AnimeSection(FrameTime, 0, 5, EaseType.Linear, null);
        assertEquals(section.GetFrom(), 0);
        assertEquals(section.GetTo(), 5);
        assertEquals(section.GetDuration(), 5);
    }
    
    /**
     * Testing of state seeking and updating.
     */
    @Test
    public void TestState()
    {
        DummyObject dummy = new DummyObject();
        
        AnimeSection section = new AnimeSection(FrameTime, 0, 5, EaseType.Linear, (progress) -> {
            dummy.Progress = progress;
        });
        
        section.SeekState(3);
        // 3 / (5 - 0) = 0.6
        assertEquals(0.6, dummy.Progress, 0.000001);
        
        section.UpdateState(4);
        // 4 / (5 - 0) = 0.8
        assertEquals(0.8, dummy.Progress, 0.000001);
        
        // Updating state past the end frame should result in max progress value.
        section.UpdateState(6);
        System.out.println("Progress: " + dummy.Progress);
        assertEquals(1, dummy.Progress, 0.000001);
    }
    
    private class DummyObject {
        public float Progress = 0f;
    }
}
