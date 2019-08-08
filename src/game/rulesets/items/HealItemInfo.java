/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

import game.rulesets.GameItem;
import game.rulesets.GamePlayer;

/**
 * Heal item.
 * @author jerrykim
 */
public class HealItemInfo extends ItemInfo {
    
    private double effect;
    
    
    public HealItemInfo(int id, ItemGrade grade, double effect, int cost)
    {
        super(
            id,
            grade,
            String.format("Heal (%s)", grade.toString()),
            String.format("Increases health by %d%% of max health.", (int)effect),
            cost,
            cost / 2,
            0
        );
        this.effect = effect;
    }
    
    public @Override void ApplyToGame(GamePlayer player, GameItem item)
    {
        player.SetCurHealth((int)(player.GetMaxHealth() * effect / 100) + player.GetCurHealth());
    }
}
