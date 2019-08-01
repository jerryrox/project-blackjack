/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Agility up item.
 * @author jerrykim
 */
public class AgilityUpItemInfo extends ItemInfo {
    
    private double effect;
    
    
    public AgilityUpItemInfo(int id, ItemGrade grade, double effect, int cost, int duration)
    {
        super(
            id,
            grade,
            String.format("Agility Up (%s)", grade.toString()),
            String.format("Decreases incoming attack's critical chance by %d%% for %d phase.", (int)effect, duration),
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
