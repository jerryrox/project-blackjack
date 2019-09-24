/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

/**
 * Types of animation wrap modes.
 * @author jerrykim
 */
public enum AnimationWrap {
    
    /**
     * Animation stops at the last frame.
     * Default.
     */
    None,
    
    /**
     * Animation resets to the beginning and stops.
     */
    Reset,
    
    /**
     * Animation loops from start.
     */
    Loop
}
