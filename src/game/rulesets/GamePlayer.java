/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.data.Yieldable;
import game.debug.Debug;
import game.entities.UserEntity;
import game.rulesets.items.ItemInfo;
import game.utils.Random;
import java.util.ArrayList;

/**
 * Representation of game player state.
 * @author jerrykim
 */
public class GamePlayer {
    
    /**
     * The user instance being wrapped over.
     */
    protected UserEntity user;
    
    /**
     * This player's hand of cards.
     */
    protected PlayerHand hand;
    
    /**
     * Cached max health.
     */
    protected int maxHealth;
    
    /**
     * Current health value of user.
     */
    protected int curHealth;
    
    /**
     * Whether the player has drawn a card during last turn.
     */
    protected boolean didDraw;
    
    /**
     * Whether the player's first card has been revealed by opponent item.
     */
    protected boolean isRevealing;
    
    /**
     * Array of game items affecting the player.
     */
    protected ArrayList<GameItem> items = new ArrayList<>();
    
    
    
    public GamePlayer(UserEntity user)
    {
        this.user = user;
        this.hand = new PlayerHand(this);
        ResetState();
    }
    
    /**
     * Returns whether the player is human controlled.
     * @return 
     */
    public boolean IsHuman() { return true; }
    
    /**
     * Returns the user instance used to represent this player.
     */
    public UserEntity GetUser() { return user; }
    
    /**
     * Returns this player's hand info.
     */
    public PlayerHand GetHand() { return hand; }
    
    /**
     * Returns the name of the player.
     */
    public String GetName() { return user.Username.GetValue(); }
    
    /**
     * Returns the remaining health of this player.
     */
    public int GetCurHealth() { return curHealth; }
    
    /**
     * Sets current health value.
     * @param curHealth
     */
    public void SetCurHealth(int curHealth)
    {
        if(curHealth < 0)
            curHealth = 0;
        else if(curHealth > maxHealth)
            curHealth = maxHealth;
        this.curHealth = curHealth;
    }
    
    /**
     * Returns the maximum health of this player.
     */
    public int GetMaxHealth() { return maxHealth; }
    
    /**
     * Returns whether the player is dead.
     */
    public boolean IsDead() { return curHealth <= 0; }
    
    /**
     * Returns the evaluated attack damage of this player.
     */
    public double GetDamage()
    {
        return user.GetStats().Power.GetValue() * GetDamageMultiplier();
    }
    
    /**
     * Returns the evaluate critical chance of this player.
     */
    public double GetCriticalChance()
    {
        return user.GetStats().Luck.GetValue() * (hand.GetTotalCardValue() == 21 ? 2f : 1f) * GetCritChanceMultiplier();
    }
    
    /**
     * Returns whether the player has drawn a card last turn.
     */
    public boolean DidDraw() { return didDraw; }
    
    /**
     * Returns whether this player's first card should be revealed.
     */
    public boolean IsRevealing() { return isRevealing; }
    
    /**
     * Sets reveal flag on the player's first.
     * @param isRevealing 
     */
    public void SetReveal(boolean isRevealing) { this.isRevealing = isRevealing; }
    
    /**
     * Sets didDraw flag on player.
     */
    public void SetDrawn(boolean didDraw) { this.didDraw = didDraw; }
    
    /**
     * Applies damage on this player.
     * Returns the amount of damage taken.
     */
    public int ApplyDamage(double damage, double criticalChance)
    {
        // Critical chance application
        if(Random.Range(0f, 100f) < criticalChance / GetCritResistMultiplier())
            damage *= 1.5;
        
        // Damage mitigation with armor.
        double armor = user.GetStats().Armor.GetValue() * GetArmorMultiplier();
        damage -= armor;
        
        // Ensure minimum damage applies.
        if(damage < 1)
            damage = 1;
        
        // Decrease health
        SetCurHealth(curHealth - (int)damage);
        
        return (int)damage;
    }
    
    /**
     * Applies item effect on this player.
     * Returns the GameItem instance applied to player.
     */
    public GameItem ApplyItem(ItemInfo item)
    {
        GameItem gameItem = new GameItem(item);
        item.ApplyToGame(this, gameItem);
        // If instant use, don't add to list.
        if(gameItem.GetDuration() > 0)
            items.add(gameItem);
        return gameItem;
    }
    
    /**
     * Returns all items currently in use.
     */
    public Iterable<GameItem> GetUsingItems() { return items; }
    
    /**
     * Event called when the player enters the next phase.
     */
    public void OnNextPhase()
    {
        // Decrease item duration.
        for(int i=items.size()-1; i>=0; i--)
        {
            GameItem item = items.get(i);
            item.SetDuration(item.GetDuration() - 1);
            if(item.GetDuration() <= 0)
                items.remove(i);
        }
        
        // First card is back hidden on next phase.
        isRevealing = false;
    }
    
    /**
     * Resets the player's play state.
     */
    public final void ResetState()
    {
        maxHealth = (int)user.GetStats().Endurance.GetValue();
        curHealth = maxHealth;
        didDraw = true;
        items.clear();
    }
    
    /**
     * Returns the factor at which affects damage.
     */
    private double GetDamageMultiplier()
    {
        double value = 1;
        for(int i=0; i<items.size(); i++)
            value += items.get(i).GetDamageMult();
        return value;
    }
    
    /**
     * Returns the factor at which affect defense.
     */
    private double GetArmorMultiplier()
    {
        double value = 1;
        for(int i=0; i<items.size(); i++)
            value += items.get(i).GetArmorMult();
        return value;
    }
    
    /**
     * Returns the factor at which affects critical chance.
     */
    private double GetCritChanceMultiplier()
    {
        double value = 1;
        for(int i=0; i<items.size(); i++)
            value += items.get(i).GetCritChanceMult();
        return value;
    }
    
    /**
     * Returns the factor at which affects critical resist.
     */
    private double GetCritResistMultiplier()
    {
        double value = 1;
        for(int i=0; i<items.size(); i++)
            value += items.get(i).GetCritResistMult();
        return value;
    }
}
