/*
 * Jerry Kim (18015036), 2019
 */
package game.ui.cli;

/**
 * Provides a general signature for objects not closely related to CliEngine.
 * @author jerrykim
 */
public interface ICliEngine {
    
    /**
     * Starts the engine, rendering the displayers every after user input.
     */
    void StartUpdate();
    
    /**
     * Stops running the engine.
     */
    void StopUpdate();
}
