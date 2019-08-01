/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

/**
 * Reveal utility item.
 * @author jerrykim
 */
public class RevealItemInfo extends ItemInfo {
    
    public RevealItemInfo(int id, int cost)
    {
        super(id, ItemGrade.D, "Reveal", "Reveals the opponent's first card.", cost, cost / 2, 0);
    }
}
