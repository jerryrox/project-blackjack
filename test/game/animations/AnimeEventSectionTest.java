/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

import game.data.Action;
import game.data.ActionT;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author jerrykim
 */
public class AnimeEventSectionTest {
    
    
    /**
     * Test of ActivateState method, of class AnimeEventSection.
     */
    @Test
    public void testActivateState()
    {
        DummyObject dummy = new DummyObject();
        DummyAnime anime = new DummyAnime();
        AnimeEventSection section = new AnimeEventSection(anime, 2, () -> {
            System.out.println("Callbacked");
            dummy.Callbacked = true;
        });
        
        // Updating shouldn't invoke callback when anime is not playing.
        anime.Playing = false;
        section.UpdateState(3);
        assertFalse(dummy.Callbacked);
        
        // Updating after being sought to future time shouldn't invoke callback.
        section.SeekState(3);
        section.UpdateState(4);
        assertFalse(dummy.Callbacked);
        
        // Set to playing now.
        anime.Playing = true;
        
        // Seeking should invoke callback while playing.
        section.SeekState(2);
        assertTrue(dummy.Callbacked);
        
        // Reset flag
        dummy.Callbacked = false;
        
        // Updating to time before invocation time and updating should invoke callback.
        section.SeekState(1);
        section.UpdateState(2);
        assertTrue(dummy.Callbacked);
    }
    
    private class DummyObject {
        
        public boolean Callbacked = false;
    }
    
    private class DummyAnime implements IAnime {
        
        public boolean Playing = false;

        @Override
        public int GetDuration() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public float GetTimeDuration() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int GetCurrentFrame() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean IsPlaying() {
            return Playing;
        }

        @Override
        public void AddSection(int from, int to, ActionT<Float> action) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void AddSection(int from, int to, EaseType easeType, ActionT<Float> action) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void AddEvent(int frame, Action action) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void SetWrapMode(AnimationWrap mode) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void SetStopMode(AnimationStop mode) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void Play() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void PlayAt(int frame) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void Pause() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void Stop() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void SeekTo(int frame) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void Update(float deltaTime) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
