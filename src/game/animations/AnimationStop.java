/*
 * Jerry Kim (18015036), 2019
 */
package game.animations;

/**
 * Types of animation stop modes.
 * @author jerrykim
 */
public enum AnimationStop {
    
    /**
     * No actions to take.
     */
    None,
    
    /**
     * Reset to the beginning of the animation.
     * Default.
     */
    Reset,
    
    /**
     * Reset to the end of the animaion.
     */
    End
}
