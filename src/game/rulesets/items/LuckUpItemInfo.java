/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Luck up item.
 * @author jerrykim
 */
public class LuckUpItemInfo extends ItemInfo {
    
    private double effect;
    
    
    public LuckUpItemInfo(int id, ItemGrade grade, double effect, int cost, int duration)
    {
        super(
            id,
            grade,
            String.format("Luck Up (%s)", grade.toString()),
            String.format("Increases critical chance to %d%% for %d phase.", (int)effect, duration),
            cost,
            cost / 2,
            duration
        );
        this.effect = effect;
    }
    
    public @Override void ApplyToGame(GamePlayer player, GameItem item)
    {
        item.SetCritChanceMult(effect / 100);
    }
}
