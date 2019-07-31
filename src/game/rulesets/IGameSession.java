/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

/**
 * Provides a general signature for objects which are driven by game session.
 * @author jerrykim
 */
public interface IGameSession {
    
    /**
     * Event called when a new game session should start.
     */
    void OnStartSession();
    
    /**
     * Event called when an existing game session should stop.
     */
    void OnStopSession();
}
