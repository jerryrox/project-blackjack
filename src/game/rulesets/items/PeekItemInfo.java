/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

/**
 * Peek utility item.
 * @author jerrykim
 */
public class PeekItemInfo extends ItemInfo {
    
    public PeekItemInfo(int id, int cost)
    {
        super(id, ItemGrade.D, "Peek", "Peeks the next card on the pile.", cost, cost / 2, 0);
    }
}
