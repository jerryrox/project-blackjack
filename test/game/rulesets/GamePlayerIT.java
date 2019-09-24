/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.entities.UserEntity;
import game.rulesets.items.ItemDefinitions;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jerrykim
 */
public class GamePlayerIT {
    
    /**
     * Testing initialization of GamePlayer instance.
     */
    @Test
    public void TestInit()
    {
        UserEntity user = GetDummyEntity();
        GamePlayer player = new GamePlayer(user);
        
        assertTrue(player.IsHuman());
        assertEquals(player.GetUser(), user);
        assertNotNull(player.GetHand());
        assertEquals(player.GetName(), user.Username.GetValue());
        assertTrue(player.GetMaxHealth() > 0);
        assertEquals(player.GetMaxHealth(), player.GetCurHealth());
        assertFalse(player.IsDead());
        assertTrue(player.GetDamage() > 0);
        assertTrue(player.GetCriticalChance() > 0);
    }
    
    /**
     * Testing damage.
     */
    @Test
    public void TestDamage()
    {
        UserEntity user = GetDummyEntity();
        GamePlayer player = new GamePlayer(user);
        
        int curHealth = player.GetCurHealth();
        int damageDealt = player.ApplyDamage(10, 0);
        assertTrue(damageDealt > 0);
        assertTrue(player.GetCurHealth() < curHealth);
    }
    
    /**
     * Testing item application.
     */
    @Test
    public void TestItemApplication()
    {
        UserEntity user = GetDummyEntity();
        GamePlayer player = new GamePlayer(user);
        ItemDefinitions definitions = new ItemDefinitions();
        
        double dmgMultiplier = player.GetDamageMultiplier();
        assertEquals(dmgMultiplier, 1, 0.000001);
        
        GameItem applied = player.ApplyItem(definitions.DamageUpD);
        assertTrue(player.GetDamageMultiplier() > dmgMultiplier);
        
        for(GameItem item : player.GetUsingItems())
            assertEquals(item, applied);
    }
    
    /**
     * Testing item expiration after certain number of turns.
     */
    @Test
    public void TestItemExpiry()
    {
        UserEntity user = GetDummyEntity();
        GamePlayer player = new GamePlayer(user);
        ItemDefinitions definitions = new ItemDefinitions();
        
        double dmgMultiplier = player.GetDamageMultiplier();
        assertEquals(dmgMultiplier, 1, 0.000001);
        
        player.ApplyItem(definitions.DamageUpD);
        assertTrue(player.GetDamageMultiplier() > dmgMultiplier);
        
        // Advance phases by the item's duration.
        for(int i=0; i<definitions.DamageUpD.Duration; i++)
            player.OnNextPhase();
        
        // The damage multiplier should have returned.
        assertEquals(dmgMultiplier, player.GetDamageMultiplier(), 0.000001);
    }
    
    private UserEntity GetDummyEntity()
    {
        UserEntity user = new UserEntity();
        user.Username.SetValue("TestUser");
        user.Id.SetValue("user");
        return user;
    }
}
