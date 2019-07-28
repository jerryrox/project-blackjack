/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.DependencyContainer;
import game.allocation.IDependencyContainer;
import game.debug.ConsoleLogger;
import game.ui.Pivot;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class CliEngineIT {
    
    public CliEngineIT() {
    }

    /**
     * Test of Render method, of class CliEngine.
     */
    @Test
    public void testRender()
    {
        IDependencyContainer container = new DependencyContainer(new ConsoleLogger());
        
        CliEngine engine = null;
        container.Cache(engine = new CliEngine(container));
        
        DummyDisplayer displayer = new DummyDisplayer();
        engine.AddDisplay(displayer);
        
        engine.Render();
    }
    
    private class DummyDisplayer extends CliDisplayer {
    
        public @Override void Render(CliBuffer buffer)
        {
            int width = buffer.GetWidth();
            int height = buffer.GetHeight();
            
            for(int y=1; y<height; y++)
            {
                buffer.SetBuffer('|', 1, y);
                buffer.SetBuffer('|', width, y);
            }
            for(int i=0; i<buffer.GetWidth(); i++)
                buffer.SetBuffer('=', i+1, 1);
            for(int i=0; i<buffer.GetWidth(); i++)
                buffer.SetBuffer('=', i+1, height);
            
            buffer.SetBuffer("Test text center", width / 2, 2, Pivot.Center);
            buffer.SetBuffer("Test text left", 2, 2, Pivot.Left);
            buffer.SetBuffer("Test text right", width-1, 2, Pivot.Right);
            
            super.Render(buffer);
        }
    }
}
