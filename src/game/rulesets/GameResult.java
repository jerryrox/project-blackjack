/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

/**
 * Holds result information of a single battle (session).
 * @author jerrykim
 */
public class GameResult {
    
    /**
     * The game mode which the result was made from.
     */
    public final GameModes Mode;
    
    /**
     * Type of result.
     */
    public final GameResultTypes ResultType;
    
    /**
     * Amount of rewards to be given to user.
     */
    public final int Rewards;
    
    /**
     * Difficulty of the opponent AI.
     */
    public final int Difficulty;
    
    
    public GameResult(GameModes mode, GameResultTypes resultType, int reward, int difficulty)
    {
        Mode = mode;
        ResultType = resultType;
        Rewards = reward;
        Difficulty = difficulty;
    }
}
