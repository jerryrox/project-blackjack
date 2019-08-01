/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

/**
 * Return utility item.
 * @author jerrykim
 */
public class ReturnItemInfo extends ItemInfo {
    
    public ReturnItemInfo(int id, int cost)
    {
        super(id, ItemGrade.D, "Return", "Returns the initiator's last drawn card to the pile, and reshuffles it.", cost, cost / 2, 0);
    }
}
