/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Damage up item.
 * @author jerrykim
 */
public class DamageUpItemInfo extends ItemInfo {
    
    private double effect;
    
    
    public DamageUpItemInfo(int id, ItemGrade grade, double effect, int cost, int duration)
    {
        super(
            id,
            grade,
            String.format("Damage Up (%s)", grade.toString()),
            String.format("Increases damage to %d%% for %d phase.", (int)effect, duration),
            cost,
            cost / 2,
            duration
        );
        this.effect = effect;
    }
    
    public @Override void ApplyToGame(GamePlayer player, GameItem item)
    {
        item.SetDamageMult(effect / 100);
    }
}
