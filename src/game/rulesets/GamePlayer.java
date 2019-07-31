/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.debug.Debug;
import game.entities.User;
import game.utils.Random;

/**
 * Representation of game player state.
 * @author jerrykim
 */
public class GamePlayer {
    
    /**
     * The user instance being wrapped over.
     */
    protected User user;
    
    /**
     * Cached max health.
     */
    protected int maxHealth;
    
    /**
     * Current health value of user.
     */
    protected int curHealth;
    
    
    
    public GamePlayer(User user)
    {
        this.user = user;
        ResetState();
    }
    
    /**
     * Returns the user instance used to represent this player.
     */
    public User GetUser() { return user; }
    
    /**
     * Returns the name of the player.
     */
    public String GetName() { return user.GetUsername(); }
    
    /**
     * Returns the remaining health of this player.
     */
    public int GetCurHealth() { return curHealth; }
    
    /**
     * Returns the maximum health of this player.
     */
    public int GetMaxHealth() { return maxHealth; }
    
    /**
     * Applies damage on this player.
     */
    public void ApplyDamage(double damage, double criticalChance)
    {
        // Critical chance application
        if(Random.Range(0f, 100f) < criticalChance)
            damage *= 1.5;
        
        // Damage mitigation with armor.
        double armor = user.GetStats().Armor.GetValue();
        damage -= armor;
        
        // Decrease health
        curHealth -= (int)damage;
        if(curHealth < 0)
            curHealth = 0;
    }
    
    /**
     * Resets the player's play state.
     */
    public final void ResetState()
    {
        maxHealth = (int)user.GetStats().Endurance.GetValue();
        curHealth = maxHealth;
    }
}
