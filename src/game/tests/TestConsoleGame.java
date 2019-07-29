/*
 * Jerry Kim (18015036), 2019
 */
package game.tests;

import game.BaseGame;
import game.ConsoleGame;
import game.allocation.DependencyContainer;
import game.allocation.IDependencyContainer;
import game.allocation.InitWithDependency;
import game.allocation.ReceivesDependency;
import game.debug.ConsoleLogger;
import game.debug.Debug;
import game.debug.ILogger;
import game.ui.Pivot;
import game.ui.cli.CliBuffer;
import game.ui.cli.CliDisplayer;
import game.ui.cli.CliEngine;
import game.ui.cli.CliRoot;
import game.ui.cli.ICliEngine;
import game.ui.cli.commands.ArgumentTypes;
import game.ui.cli.commands.CommandInfo;

/**
 *
 * @author jerrykim
 */
public class TestConsoleGame extends ConsoleGame {
    
    public TestConsoleGame(ILogger logger)
    {
        super(logger);
    }
    
    protected @Override void PostInitialize()
    {
        super.PostInitialize();
        
        DummyDisplayer displayer = new DummyDisplayer();
        cliEngine.GetRootDisplay().AddChild(displayer);
    }
    
    protected @Override void OnStart()
    {   
        cliEngine.StartUpdate();
    }
    
    private class DummyDisplayer extends CliDisplayer {
        
        private String message;
        
        @InitWithDependency
        public void Init()
        {
            CommandInfo test0 = new CommandInfo("test0", (arguments) -> {
                message = "Entered test 0";
            });
            CommandInfo test1 = new CommandInfo("test1", (arguments) -> {
                message = String.format("Entered test 1. age: %d, name: %s", arguments.GetInt("age"), arguments.Get("name"));
            });
            test1.SetDescription("This is my description for test1")
                .SetArgument("age", ArgumentTypes.Int)
                .SetArgument("name", ArgumentTypes.String);
            
            commands.AddCommand(test0);
            commands.AddCommand(test1);
            
            commands.SetEnable(true);
            commands.SetPropagate(true);
        }
    
        public @Override void Render(CliBuffer buffer)
        {
            int width = buffer.GetWidth();
            int height = buffer.GetHeight();
            
            for(int y=0; y<height; y++)
            {
                buffer.SetBuffer('|', 0, y);
                buffer.SetBuffer('|', width-1, y);
            }
            for(int i=0; i<width; i++)
                buffer.SetBuffer('=', i, 0);
            for(int i=0; i<width; i++)
                buffer.SetBuffer('=', i, height-1);
            
            buffer.SetBuffer("Test text center", width / 2, 1, Pivot.Center);
            buffer.SetBuffer("Test text left", 1, 1, Pivot.Left);
            buffer.SetBuffer("Test text right", width-2, 1, Pivot.Right);
            if(message != null)
                buffer.SetBuffer(message, width/2, height/2, Pivot.Center);
            
            super.Render(buffer);
        }
    }
}
