/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

import game.allocation.InitWithDependency;
import game.debug.Debug;
import game.ui.cli.commands.CommandInfo;

/**
 * The root displayer object.
 * @author jerrykim
 */
public class CliRoot extends CliDisplayer {
    
    
    public CliRoot()
    {
        SetActive(true);
    }
    
    @InitWithDependency
    public void Init(ICliEngine engine)
    {
//        commands.AddCommand(new CommandInfo("quit", (args) -> {
//            engine.StopUpdate();
//        }));
//        
//        commands.SetEnable(true);
    }
}
