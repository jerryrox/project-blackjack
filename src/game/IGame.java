/*
 * Jerry Kim (18015036), 2019
 */
package game;

/**
 * Provides interface of the various BaseGame types in user's point of view.
 * @author jerrykim
 */
public interface IGame {
    
    /**
     * Returns the current runtime mode.
     */
    RuntimeMode GetRuntime();
    
    /**
     * Returns the application version.
     */
    String GetVersion();
    
    /**
     * Forcefully quits application.
     */
    void ForceQuit();
    
    /**
     * Gracefully quits application.
     */
    void Quit();
}
