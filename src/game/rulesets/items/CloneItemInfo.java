/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

/**
 * Clone utility item.
 * @author jerrykim
 */
public class CloneItemInfo extends ItemInfo {
    
    public CloneItemInfo(int id, int cost)
    {
        super(id, ItemGrade.D, "Clone", "Clones the initiator's last drawn card and adds to the hand.", cost, cost / 2, 0);
    }
}
