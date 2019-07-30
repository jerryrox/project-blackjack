/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets;

import game.data.Yieldable;
import java.util.ArrayList;

/**
 * Holds static definitions of an item used in the game.
 * @author jerrykim
 */
public class ItemDefinitions {
    
    public final ItemInfo DamageUpD;
    public final ItemInfo DamageUpC;
    public final ItemInfo DamageUpB;
    public final ItemInfo DamageUpA;
    public final ItemInfo DamageUpS;
    public final ItemInfo DamageUpSS;
    public final ItemInfo DamageUpSSS;
    
    public final ItemInfo LuckUpD;
    public final ItemInfo LuckUpC;
    public final ItemInfo LuckUpB;
    public final ItemInfo LuckUpA;
    public final ItemInfo LuckUpS;
    public final ItemInfo LuckUpSS;
    public final ItemInfo LuckUpSSS;
    
    public final ItemInfo ArmorUpD;
    public final ItemInfo ArmorUpC;
    public final ItemInfo ArmorUpB;
    public final ItemInfo ArmorUpA;
    public final ItemInfo ArmorUpS;
    public final ItemInfo ArmorUpSS;
    public final ItemInfo ArmorUpSSS;
    
    public final ItemInfo AgilityUpD;
    public final ItemInfo AgilityUpC;
    public final ItemInfo AgilityUpB;
    public final ItemInfo AgilityUpA;
    public final ItemInfo AgilityUpS;
    public final ItemInfo AgilityUpSS;
    public final ItemInfo AgilityUpSSS;
    
    public final ItemInfo HealD;
    public final ItemInfo HealC;
    public final ItemInfo HealB;
    public final ItemInfo HealA;
    public final ItemInfo HealS;
    public final ItemInfo HealSS;
    public final ItemInfo HealSSS;
    
    public final ItemInfo Peek;
    public final ItemInfo Reveal;
    public final ItemInfo Return;
    public final ItemInfo Clone;
    
    private ArrayList<ItemInfo> items = new ArrayList<ItemInfo>();
    
    
    public ItemDefinitions()
    {
        items.add(DamageUpD = new ItemInfo(1, ItemTypes.Offensive, "Damage Up (D)", "Increases power value to 115% for 1 phase.", 100, 50, 1));
        items.add(DamageUpC = new ItemInfo(2, ItemTypes.Offensive, "Damage Up (C)", "Increases power value to 120% for 2 phases.", 200, 100, 2));
        items.add(DamageUpB = new ItemInfo(3, ItemTypes.Offensive, "Damage Up (B)", "Increases power value to 125% for 2 phases.", 400, 200, 2));
        items.add(DamageUpA = new ItemInfo(4, ItemTypes.Offensive, "Damage Up (A)", "Increases power value to 130% for 2 phases.", 700, 350, 2));
        items.add(DamageUpS = new ItemInfo(5, ItemTypes.Offensive, "Damage Up (S)", "Increases power value to 135% for 3 phases.", 1100, 550, 3));
        items.add(DamageUpSS = new ItemInfo(6, ItemTypes.Offensive, "Damage Up (SS)", "Increases power value to 140% for 3 phases.", 1600, 800, 3));
        items.add(DamageUpSSS = new ItemInfo(7, ItemTypes.Offensive, "Damage Up (SSS)", "Increases power value to 150% for 3 phases.", 2200, 1100, 3));

        items.add(LuckUpC = new ItemInfo(11, ItemTypes.Offensive, "Luck Up (D)", "Increases luck value to 105% for 1 phase.", 100, 50, 1));
        items.add(LuckUpB = new ItemInfo(12, ItemTypes.Offensive, "Luck Up (C)", "Increases luck value to 110% for 2 phases.", 200, 100, 2));
        items.add(LuckUpA = new ItemInfo(13, ItemTypes.Offensive, "Luck Up (B)", "Increases luck value to 115% for 2 phases.", 400, 200, 2));
        items.add(LuckUpD = new ItemInfo(14, ItemTypes.Offensive, "Luck Up (A)", "Increases luck value to 120% for 2 phases.", 700, 350, 2));
        items.add(LuckUpS = new ItemInfo(15, ItemTypes.Offensive, "Luck Up (S)", "Increases luck value to 125% for 3 phases.", 1100, 550, 3));
        items.add(LuckUpSS = new ItemInfo(16, ItemTypes.Offensive, "Luck Up (SS)", "Increases luck value to 130% for 3 phases.", 1600, 800, 3));
        items.add(LuckUpSSS = new ItemInfo(17, ItemTypes.Offensive, "Luck Up (SSS)", "Increases luck value to 140% for 3 phases.", 2200, 1100, 3));

        items.add(ArmorUpD = new ItemInfo(21, ItemTypes.Defensive, "Armor Up (D)", "Increases armor value to 110% for 1 phase.", 100, 50, 1));
        items.add(ArmorUpC = new ItemInfo(22, ItemTypes.Defensive, "Armor Up (C)", "Increases armor value to 115% for 2 phases.", 200, 100, 2));
        items.add(ArmorUpB = new ItemInfo(23, ItemTypes.Defensive, "Armor Up (B)", "Increases armor value to 120% for 2 phases.", 400, 200, 2));
        items.add(ArmorUpA = new ItemInfo(24, ItemTypes.Defensive, "Armor Up (A)", "Increases armor value to 125% for 2 phases.", 700, 350, 2));
        items.add(ArmorUpS = new ItemInfo(25, ItemTypes.Defensive, "Armor Up (S)", "Increases armor value to 130% for 3 phases.", 1100, 550, 3));
        items.add(ArmorUpSS = new ItemInfo(26, ItemTypes.Defensive, "Armor Up (SS)", "Increases armor value to 135% for 3 phases.", 1600, 800, 3));
        items.add(ArmorUpSSS = new ItemInfo(27, ItemTypes.Defensive, "Armor Up (SSS)", "Increases armor value to 145% for 3 phases.", 2200, 1100, 3));
        
        items.add(AgilityUpD = new ItemInfo(31, ItemTypes.Defensive, "Agility Up (D)", "Decreases incoming attack's critical chance to 95% for 1 phase.", 100, 50, 1));
        items.add(AgilityUpC = new ItemInfo(32, ItemTypes.Defensive, "Agility Up (C)", "Decreases incoming attack's critical chance to 90% for 2 phases.", 200, 100, 2));
        items.add(AgilityUpB = new ItemInfo(33, ItemTypes.Defensive, "Agility Up (B)", "Decreases incoming attack's critical chance to 85% for 2 phases.", 400, 200, 2));
        items.add(AgilityUpA = new ItemInfo(34, ItemTypes.Defensive, "Agility Up (A)", "Decreases incoming attack's critical chance to 80% for 2 phases.", 700, 350, 2));
        items.add(AgilityUpS = new ItemInfo(35, ItemTypes.Defensive, "Agility Up (S)", "Decreases incoming attack's critical chance to 75% for 3 phases.", 1100, 550, 3));
        items.add(AgilityUpSS = new ItemInfo(36, ItemTypes.Defensive, "Agility Up (SS)", "Decreases incoming attack's critical chance to 70% for 3 phases.", 1600, 800, 3));
        items.add(AgilityUpSSS = new ItemInfo(37, ItemTypes.Defensive, "Agility Up (SSS)", "Decreases incoming attack's critical chance to 60% for 3 phases.", 2200, 1100, 3));
        
        items.add(HealD = new ItemInfo(41, ItemTypes.Utility, "Heal (D)", "Increases health by 6% of max health.", 100, 50, 0));
        items.add(HealC = new ItemInfo(42, ItemTypes.Utility, "Heal (C)", "Increases health by 10% of max health.", 200, 100, 0));
        items.add(HealB = new ItemInfo(43, ItemTypes.Utility, "Heal (B)", "Increases health by 14% of max health.", 400, 200, 0));
        items.add(HealA = new ItemInfo(44, ItemTypes.Utility, "Heal (A)", "Increases health by 18% of max health.", 700, 350, 0));
        items.add(HealS = new ItemInfo(45, ItemTypes.Utility, "Heal (S)", "Increases health by 22% of max health.", 1100, 550, 0));
        items.add(HealSS = new ItemInfo(46, ItemTypes.Utility, "Heal (SS)", "Increases health by 26% of max health.", 1600, 800, 0));
        items.add(HealSSS = new ItemInfo(47, ItemTypes.Utility, "Heal (SSS)", "Increases health by 32% of max health.", 2200, 1100, 0));
        
        items.add(Peek = new ItemInfo(1001, ItemTypes.Utility, "Peek", "Peeks the next card on the pile.", 2500, 1250, 0));
        items.add(Reveal = new ItemInfo(1002, ItemTypes.Utility, "Reveal", "Reveals the opponent's first card.", 2500, 1250, 0));
        items.add(Return = new ItemInfo(1003, ItemTypes.Utility, "Return", "Returns the initiator's last drawn card to the pile, and reshuffles it.", 2500, 1250, 0));
        items.add(Clone = new ItemInfo(1004, ItemTypes.Utility, "Clone", "Clones the initiator's last drawn card and adds to the hand.", 5000, 2500, 0));
    }
    
    /**
     * Returns the item info instance of specified id.
     * @param id
     * @return 
     */
    public ItemInfo GetInfo(int id)
    {
        for(int i=0; i<items.size(); i++)
        {
            if(items.get(i).Id == id)
                return items.get(i);
        }
        return null;
    }
    
    /**
     * Returns a page of item infos.
     * @return 
     */
    public Iterable<ItemInfo> GetInfoPage(int count, int pageIndex)
    {
        return new Yieldable<ItemInfo>(yield -> {
            int startIndex = pageIndex * count;
            int end = startIndex + count;
            for(int i=startIndex; i<end; i++)
                yield.Return(items.get(i));
        });
    }
    
    /**
     * Returns the number of item definitions in the list.
     * @return 
     */
    public int GetCount() { return items.size(); }
}
