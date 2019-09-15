/*
 * Jerry Kim (18015036), 2019
 */
package game.rulesets.items;

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
        items.add(DamageUpD = new DamageUpItemInfo(1, ItemGrade.D, 115, 50, 1));
        items.add(DamageUpC = new DamageUpItemInfo(2, ItemGrade.C, 120, 100, 2));
        items.add(DamageUpB = new DamageUpItemInfo(3, ItemGrade.B, 125, 200, 2));
        items.add(DamageUpA = new DamageUpItemInfo(4, ItemGrade.A, 130, 350, 2));
        items.add(DamageUpS = new DamageUpItemInfo(5, ItemGrade.S, 135, 550, 3));
        items.add(DamageUpSS = new DamageUpItemInfo(6, ItemGrade.SS, 140, 800, 3));
        items.add(DamageUpSSS = new DamageUpItemInfo(7, ItemGrade.SSS, 150, 1100, 3));

        items.add(LuckUpC = new LuckUpItemInfo(11, ItemGrade.D, 105, 50, 1));
        items.add(LuckUpB = new LuckUpItemInfo(12, ItemGrade.C, 110, 100, 2));
        items.add(LuckUpA = new LuckUpItemInfo(13, ItemGrade.B, 115, 200, 2));
        items.add(LuckUpD = new LuckUpItemInfo(14, ItemGrade.A, 120, 350, 2));
        items.add(LuckUpS = new LuckUpItemInfo(15, ItemGrade.S, 125, 550, 3));
        items.add(LuckUpSS = new LuckUpItemInfo(16, ItemGrade.SS, 130, 800, 3));
        items.add(LuckUpSSS = new LuckUpItemInfo(17, ItemGrade.SSS, 140, 1100, 3));

        items.add(ArmorUpD = new ArmorUpItemInfo(21, ItemGrade.D, 110, 50, 1));
        items.add(ArmorUpC = new ArmorUpItemInfo(22, ItemGrade.C, 115, 100, 2));
        items.add(ArmorUpB = new ArmorUpItemInfo(23, ItemGrade.B, 120, 200, 2));
        items.add(ArmorUpA = new ArmorUpItemInfo(24, ItemGrade.A, 125, 350, 2));
        items.add(ArmorUpS = new ArmorUpItemInfo(25, ItemGrade.S, 130, 550, 3));
        items.add(ArmorUpSS = new ArmorUpItemInfo(26, ItemGrade.SS, 135, 800, 3));
        items.add(ArmorUpSSS = new ArmorUpItemInfo(27, ItemGrade.SSS, 145, 1100, 3));
        
        items.add(AgilityUpD = new AgilityUpItemInfo(31, ItemGrade.D, 5, 50, 1));
        items.add(AgilityUpC = new AgilityUpItemInfo(32, ItemGrade.C, 10, 100, 2));
        items.add(AgilityUpB = new AgilityUpItemInfo(33, ItemGrade.B, 15, 200, 2));
        items.add(AgilityUpA = new AgilityUpItemInfo(34, ItemGrade.A, 20, 350, 2));
        items.add(AgilityUpS = new AgilityUpItemInfo(35, ItemGrade.S, 25, 550, 3));
        items.add(AgilityUpSS = new AgilityUpItemInfo(36, ItemGrade.SS, 30, 800, 3));
        items.add(AgilityUpSSS = new AgilityUpItemInfo(37, ItemGrade.SSS, 40, 1100, 3));
        
        items.add(HealD = new HealItemInfo(41, ItemGrade.D, 6, 50));
        items.add(HealC = new HealItemInfo(42, ItemGrade.C, 10, 100));
        items.add(HealB = new HealItemInfo(43, ItemGrade.B, 14, 200));
        items.add(HealA = new HealItemInfo(44, ItemGrade.A, 18, 350));
        items.add(HealS = new HealItemInfo(45, ItemGrade.S, 22, 550));
        items.add(HealSS = new HealItemInfo(46, ItemGrade.SS, 26, 800));
        items.add(HealSSS = new HealItemInfo(47, ItemGrade.SSS, 32, 1100));
        
        items.add(Peek = new PeekItemInfo(1001, 150));
        items.add(Reveal = new RevealItemInfo(1002, 150));
        items.add(Return = new ReturnItemInfo(1003, 300));
        items.add(Clone = new CloneItemInfo(1004, 300));
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
     * Returns all item infos.
     */
    public Iterable<ItemInfo> GetAllInfos() { return items; }
    
    /**
     * Returns the number of item definitions in the list.
     * @return 
     */
    public int GetCount() { return items.size(); }
}
