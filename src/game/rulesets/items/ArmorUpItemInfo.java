/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Armor up item.
 * @author jerrykim
 */
public class ArmorUpItemInfo extends ItemInfo {
    
    private double effect;
    
    
    public ArmorUpItemInfo(int id, ItemGrade grade, double effect, int cost, int duration)
    {
        super(
            id,
            grade,
            String.format("Armor Up (%s)", grade.toString()),
            String.format("Increases armor value to %d%% for %d phase.", (int)effect, duration),
            cost,
            cost / 2,
            duration
        );
        this.effect = effect;
    }
    
    public @Override void ApplyToGame(GamePlayer player, GameItem item)
    {
        item.SetArmorMult(effect / 100);
    }
}
