/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.entities.User;
import game.entities.UserStats;
import game.utils.Random;

/**
 * GamePlayer variant for AI.
 * @author jerrykim
 */
public class GameAIPlayer extends GamePlayer {
    
    /**
     * Current difficulty set on the ai.
     */
    private int difficulty;
    
    
    public GameAIPlayer()
    {
        super(new User());
    }
    
    /**
     * Returns the current difficulty value of this ai.
     */
    public int GetDifficulty() { return difficulty; }
    
    /**
     * Sets the difficulty value on the ai.
     * @param difficulty 
     */
    public void SetDifficulty(int difficulty)
    {
        this.difficulty = difficulty;
        
        // Set reward for this ai.
        user.SetGold(GetReward(difficulty));
        
        // Set name.
        user.SetUsername("Bot " + Random.Range(0, 10000));
        
        // Modify stats.
        UserStats stat = user.GetStats();
        stat.Power.SetLevel((difficulty + 1) / 2);
        stat.Armor.SetLevel((difficulty + 1) / 2);
        stat.Endurance.SetLevel(difficulty / 2);
        stat.Luck.SetLevel(difficulty / 2);
        
        // Reset state.
        ResetState();
    }
    
    /**
     * Calculates the reward amount.
     */
    private int GetReward(int difficulty)
    {
        return (int)((Math.pow(difficulty, 1.6) + 10) * Random.Range(0.95f, 1.05f));
    }
}
